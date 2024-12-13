    package com.example.sm.minh.eshop.models;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import lombok.Getter;
    import lombok.Setter;
    import org.hibernate.validator.constraints.Length;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;

    import java.util.Date;
    import java.util.HashSet;
    import java.util.Set;

    @Entity(name = "product_categories")
    @Getter
    @Setter
    public class ProductCategory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;


        @Column(name = "name",length = 255,nullable = false,unique = true)
        private String name ;


        @Column(name = "slug",length = 30,nullable = false,unique = true)
        private String slug ;

        @NotNull
        @NotBlank(message = "Mô tả là bắt buộc")
        @Length(min = 6,message = "Phải có ít nhất 6 kí tựs")
        @Column(name = "description",length = 255,nullable = false)
        private String description;

        @Column(name = "created_at", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        private Date createdAt;

        @Column(name = "updated_at")
        @Temporal(TemporalType.TIMESTAMP)
        @LastModifiedDate
        private Date updatedAt;

        @Column(name = "deleted_at")
        private Date deletedAt;

        @Column(name = "is_active")
        private Boolean isActive;

        @Column(name = "image",length = 255)
        private String image;

        @ManyToMany(mappedBy = "ListProductCategories" ,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
        private Set<Product> listProducts = new HashSet<>();

        public ProductCategory(String name, String slug, String description) {
            this.name = name;
            this.slug = slug;
            this.description = description;
            this.createdAt=new Date();
        }

        public ProductCategory() {
        }
        public String getLog() {
            return this.id +" " +this.name +" "+this.slug ;
        }
        @Override
        public String toString() {
            return this.name;
        }
        public String loadImages()
        {
            if(this.image==null||this.image.isEmpty())
            {
                return "/images/products/img.png";
            }else
            {
                return "/images/categories/"+this.id+"/"+this.image;
            }
        }
    }
