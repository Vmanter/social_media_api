package com.YK.social_media.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// JBA из коробки использует Hibernate
// но все аннотации из javax.persistence
// Разработчики Hibernate не рекомендуют использовать аннотации из своего пакета и они помечены, как depricated

// Аннотации
@Entity
@Table(name = "images")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "originalFileName")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "contentType")
    private String contentType;
    @Column(name = "previewImage")
    private boolean previewImage;
    //@Lob // LONGBLOB  @Column(columnDefinition = "LONGBLOBx")
    @Column(columnDefinition = "LONGBLOB")
    private byte[] bytes;

    // Отоношения таблиц
    // нужно установить отношения одному посту = много фоторграфий
    // для этого надо использовать аннтации ManyToOne и OneToMany

    // cascade - как повлияет изменение сущности фотографии на сущность поста
    // fetch - способ загрузки EAGER - при загрузке картинки загружаем сразу все сущности
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(boolean previewImage) {
        this.previewImage = previewImage;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
