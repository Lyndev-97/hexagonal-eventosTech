package com.eventostec.api.adapters.outbound.storage;

import com.eventostec.api.domain.image.ImageFile;
import com.eventostec.api.domain.image.ImageUploadException;
import com.eventostec.api.domain.image.ImageUploaderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;
import java.util.UUID;

// Adapter de infraestrutura: ele implementa a porta de saída da aplicação.
// A camada de aplicação conhece apenas ImageUploaderPort, e não o S3 diretamente.
@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUploaderAdapter implements ImageUploaderPort {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;

    @Override
    public String uploadImage(ImageFile image) {
        String filename = UUID.randomUUID() + "-" + image.originalName();
        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName).key(filename).contentType(image.contentType()).build();
            s3Client.putObject(putOb, RequestBody.fromBytes(image.content()));
            GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(filename).build();
            return s3Client.utilities().getUrl(request).toString();
        } catch (Exception e) {
            log.error("erro ao subir arquivo: {}", e.getMessage());
            throw new ImageUploadException("Falha ao enviar imagem", e);
        }
    }
}
