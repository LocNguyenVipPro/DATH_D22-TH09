package com.example.locnguyen.techzone.services;

import java.io.IOException;
import java.util.*;

import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.locnguyen.techzone.dto.ProductDTO;
import com.example.locnguyen.techzone.exceptions.CategoryProductException;
import com.example.locnguyen.techzone.exceptions.ProductException;
import com.example.locnguyen.techzone.models.Cart;
import com.example.locnguyen.techzone.models.CartLineItem;
import com.example.locnguyen.techzone.models.Product;
import com.example.locnguyen.techzone.models.ProductCategory;
import com.example.locnguyen.techzone.repositories.*;
import com.example.locnguyen.techzone.utilities.FileUploadUltil;

@Service
public class ProductService {
    private static final Integer INT_PAGE_SIZE = 8;

    @Autowired
    private ProductRepository producsRepository;

    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private CartReposttory cartReposttory;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<ProductDTO> findAll(Integer categoryId, String search, Boolean isHide) {
        if (search == null || search.trim().isEmpty()) search = null;
        List<ProductDTO> list = new ArrayList<>();

        if (categoryId != null && search != null) {

            return toProductDTO(this.producsRepository.findAll(categoryId, search, isHide));
        } else if (categoryId != null) {
            return toProductDTO(this.producsRepository.findAllByCategoryId(categoryId, isHide));
        } else if (search != null && !search.trim().isEmpty()) {

            return toProductDTO(this.producsRepository.findAll(search, isHide));
        }
        return toProductDTO(this.producsRepository.findAllProductsAndTotalSold(isHide));
    }

    public List<ProductCategory> findAllCategory() {
        return this.productCategoryRepository.findAll(true);
    }

    public List<ProductCategory> findAllCategoryContainProduct() {
        return this.productCategoryRepository.findAllCategoriesWithProducts();
    }

    public ProductCategory getCategoryById(Integer id) throws CategoryProductException {
        Optional<ProductCategory> optionalProductCategory = this.productCategoryRepository.findById(id);
        return optionalProductCategory.orElseThrow(
                () -> new CategoryProductException("Cannot Find Category With ID :" + id));
    }

    public Product save(Product product) {
        return this.producsRepository.save(product);
    }

    public void delete(Integer id) throws ProductException {
        Product deleteProduct = this.findById(id);

        // unlink Product from Product
        deleteCartLineItemsByProduct(deleteProduct);

        // Mark the product as deleted and inactive
        deleteProduct.setDeletedAt(new Date());
        deleteProduct.setIsActive(false);
        this.producsRepository.save(deleteProduct);
    }

    // Method to delete cart items related to a product
    private void deleteCartLineItemsByProduct(Product product) {

        for (CartLineItem cartLineItem : this.cartLineItemRepositoty.findByProductId(product)) {
            Cart cart = cartLineItem.getCartId();

            // Update cart information
            updateCartInfo(cart, cartLineItem);

            // Update and save cart information
            this.cartReposttory.save(cart);

            // Delete cart item
            this.cartLineItemRepositoty.delete(cartLineItem);
        }
    }

    // Method to update cart information after deleting a cart item related to a product
    private void updateCartInfo(Cart cart, CartLineItem cartLineItem) {
        cart.setTaxAmount(cart.getTaxAmount() - cartLineItem.getTaxTotalAmount());
        cart.setCountItem(cart.getCountItem() - cartLineItem.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount() - cartLineItem.getTotalAmount());
        cart.setCreatedAt(new Date());
        cartLineItem.setCartId(null);
    }

    public void restoreProduct(Integer id) throws ProductException {
        Optional<Product> productOption = producsRepository.findProductByIsActiveIsFalse(id);

        if (productOption.isPresent()) {
            Product product = productOption.get();
            for (ProductCategory productCategory : product.getListProductCategories()) {
                if (!productCategory.getIsActive()) {
                    throw new ProductException(
                            "Cannot restore " + productCategory.getName() + " because it does not exist");
                }
            }

            updateCartAndCAndCartLineItem(product);
            product.setIsActive(true);
            producsRepository.save(product);
        } else {
            throw new ProductException("Cannot find product with ID: " + id);
        }
    }

    public void updateCartAndCAndCartLineItem(Product product) {

        for (CartLineItem cartLineItem : this.cartLineItemRepositoty.findByProductId(product)) {
            Cart updateCart = cartLineItem.getCartId();
            updateCart.setTaxAmount(updateCart.getTaxAmount() - cartLineItem.getTaxTotalAmount());
            updateCart.setCountItem(updateCart.getCountItem() - cartLineItem.getQuantity());
            updateCart.setTotalAmount(updateCart.getTotalAmount() - cartLineItem.getTotalAmount());
            updateCart.setUpdatedAt(new Date());
            cartLineItem.setCartId(null);
            this.cartLineItemRepositoty.save(cartLineItem);
            this.cartReposttory.save(updateCart);
            this.cartLineItemRepositoty.delete(cartLineItem);
        }
    }

    public Product findById(Integer id) throws ProductException {
        Optional<Product> productOption = this.producsRepository.findById(id);
        return productOption.orElseThrow(() -> new ProductException("Cannot Found Products With Id : " + id));
    }

    public ArrayList<ProductDTO> productOrderMost() {
        Pageable pageable = PageRequest.of(0, INT_PAGE_SIZE);
        List<Object[]> listProductOrderMost = this.orderLineItemRepository.findProductsOrderedMost(pageable);
        ArrayList<ProductDTO> productDTOS = new ArrayList<>();

        for (Object[] obj : listProductOrderMost) {

            Product product = (Product) obj[0];
            Long quantityPurchase = (Long) obj[1];
            ProductDTO productDTO = new ProductDTO(product, quantityPurchase);
            productDTOS.add(productDTO);
        }

        return productDTOS;
    }

    public Product setDataProduct(Product product, Product productInForm) throws IOException {
        product.setUpdatedAt(new Date());
        product.setContent(productInForm.getContent());
        product.setSku(productInForm.getSku());
        product.setListProductCategories(productInForm.getListProductCategories());
        product.setPrice(productInForm.getPrice());
        product.setName(productInForm.getName());
        product.setDiscountPrice(productInForm.getDiscountPrice());
        product.setTax(productInForm.getTax());
        product.setIsActive(productInForm.getIsActive());
        return product;
    }

    public Product saveImage(Product product, MultipartFile multipartFile) throws IOException {

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            fileName = fileName.replace(' ', '_');
            product.setImage(fileName);
            this.save(product);
            String directory = "public/images/products/" + product.getId();
            FileUploadUltil.saveFile(directory, fileName, multipartFile, null);
        } else {
            this.save(product);
        }

        return product;
    }

    public void saveProduct(Product product, MultipartFile multipartFile) throws IOException, ProductException {

        if (product.getId() != null) {
            Product productInDb = this.findById(product.getId());
            productInDb = this.setDataProduct(productInDb, product);
            productInDb = this.saveImage(productInDb, multipartFile);
            this.saveImage(productInDb, multipartFile);
        } else {
            product = trimProduct(product);
            product.setCreatedAt(new Date());
            this.saveImage(product, multipartFile);
        }
    }

    public Product trimProduct(Product product) {
        product.setName(product.getName().trim());
        product.setContent(product.getContent().trim());
        product.setSku(product.getSku().trim());
        return product;
    }

    public boolean checkNameAndSkuUnique(
            String name,
            String sku,
            Integer id,
            String nameField,
            String skuField,
            ConstraintValidatorContext context) {
        String nameErrorMessage = null;
        String skuErrorMessage = null;

        if (id == null || id == 0) {
            if (producsRepository.findProductByName(name) != null) {
                nameErrorMessage = "Product with name " + name + " already exists";
            }
            if (producsRepository.findProductBySku(sku) != null) {
                skuErrorMessage = "Product with SKU " + sku + " already exists";
            }
        } else {
            Product existingProduct = producsRepository.findById(id).orElse(null);
            if (existingProduct == null) {
                nameErrorMessage = "Product not found";
                skuErrorMessage = "Product not found";
            } else {
                Product checkProductByName = producsRepository.findProductByName(name);
                Product checkProductBySku = producsRepository.findProductBySku(sku);

                if (checkProductBySku != null && !existingProduct.getSku().equals(sku)) {
                    skuErrorMessage = "Product SKU conflicts with existing products";
                }
                if (checkProductByName != null && !existingProduct.getName().equals(name)) {
                    nameErrorMessage = "Product name conflicts with existing products";
                }
            }
        }

        if (nameErrorMessage != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(nameErrorMessage)
                    .addPropertyNode(nameField)
                    .addConstraintViolation();
        }

        if (skuErrorMessage != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(skuErrorMessage)
                    .addPropertyNode(skuField)
                    .addConstraintViolation();
        }

        return nameErrorMessage == null && skuErrorMessage == null;
    }

    // format sale product
    public String formatQuantity(long quantity) {
        if (quantity >= 1000000) {
            double result = quantity / 1000000.0;
            return (result % 1 == 0) ? String.format("%.0fm", result) : String.format("%.1fm", result);
        } else if (quantity >= 1000) {
            double result = quantity / 1000.0;
            return (result % 1 == 0) ? String.format("%.0fk", result) : String.format("%.1fk", result);
        } else {
            return String.valueOf(quantity);
        }
    }

    public int calculateRoundedPercent(ProductDTO productDTO) {
        double discountPercent =
                ((productDTO.getProduct().getPrice() - productDTO.getProduct().getDiscountPrice())
                                / productDTO.getProduct().getPrice())
                        * 100;
        int roundedPercent = (int) Math.round(discountPercent);
        double decimalPart = discountPercent - roundedPercent;
        if (decimalPart >= 0.5) {
            roundedPercent++;
        }
        return roundedPercent;
    }

    public ArrayList<ProductDTO> toProductDTO(List<Object[]> objectListProductDTO) {

        ArrayList<ProductDTO> productDTOS = new ArrayList<>();
        for (Object[] obj : objectListProductDTO) {
            Product product = (Product) obj[0];
            Long quantityPurchase = (Long) obj[1];
            ProductDTO productDTO = new ProductDTO(product, quantityPurchase);
            productDTOS.add(productDTO);
        }

        return productDTOS;
    }

    public int[] getRangePrice(String rangePrice) {
        int[] pair = new int[2];

        if (rangePrice.equals("0-1k")) {
            pair[0] = 0;
            pair[1] = 1000;

        } else if (rangePrice.equals("1k-2k")) {
            pair[0] = 1000;
            pair[1] = 2000;
        } else if (rangePrice.equals("2k-3k")) {
            pair[0] = 2000;
            pair[1] = 3000;
        } else if (rangePrice.equals("3k-4k")) {
            pair[0] = 3000;
            pair[1] = 4000;
        } else {
            pair[0] = 4000;
            pair[1] = 500000;
        }

        return pair;
    }

    public int[] getRangePercent(String rangeSalePercent) {
        int[] pair = new int[2];

        if (rangeSalePercent.equals("0%-20%")) {
            pair[0] = 0;
            pair[1] = 20;

        } else if (rangeSalePercent.equals("20%-40%")) {
            pair[0] = 20;
            pair[1] = 40;
        } else if (rangeSalePercent.equals("40%-60%")) {
            pair[0] = 40;
            pair[1] = 60;
        } else if (rangeSalePercent.equals("60%-80%")) {
            pair[0] = 60;
            pair[1] = 80;
        } else {
            pair[0] = 80;
            pair[1] = 100;
        }

        return pair;
    }

    public List<ProductDTO> findByPrice(String rangePrice, String rangeSalePercent, String categoryId) {
        List<ProductDTO> productDTOArrayList = new ArrayList<>();
        int[] priceRange = null;
        int[] saleRange = null;
        Integer categorySelected = null;

        if (categoryId != null) {
            categorySelected = Integer.parseInt(categoryId);
        }

        if (rangeSalePercent != null) {
            saleRange = getRangePercent(rangeSalePercent);
        }

        if (rangePrice != null) {
            priceRange = getRangePrice(rangePrice);
        }

        Integer saleRangeMin = null;
        Integer saleRangeMax = null;

        if (saleRange != null) {
            saleRangeMin = saleRange[0];
            saleRangeMax = saleRange[1];
        }

        Integer priceRangeMin = null;
        Integer priceRangeMax = null;

        if (priceRange != null) {
            priceRangeMin = priceRange[0];
            priceRangeMax = priceRange[1];
        }

        productDTOArrayList = toProductDTO(this.producsRepository.findByPriceSalePercentAndCategory(
                priceRangeMin, priceRangeMax, saleRangeMin, saleRangeMax, categorySelected));
        return productDTOArrayList;
    }
}
