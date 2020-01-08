package com.psawesome.basepackage.rabbitmq.lrpart_06;

import com.psawesome.basepackage.rabbitmq.lrpart_06.images.entity.Image;
import com.psawesome.basepackage.rabbitmq.lrpart_06.images.repo.CommentReaderRepository;
import com.psawesome.basepackage.rabbitmq.lrpart_06.images.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06
 * author: PS
 * DATE: 2020-01-03 금요일 00:04
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    public static final String BASE_PATH = "/images";
    public static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;
    private final CommentReaderRepository repository;

    @GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename) {
        return imageService.findOneImage(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok()
                                .contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    } catch (IOException e) {
                        return ResponseEntity.badRequest()
                                .body("couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }

    @PostMapping(value = BASE_PATH)
    public Mono<String> createFile(@RequestPart(name = "file") Flux<FilePart> files) {
        return imageService.createImage(files)
                .then(Mono.just("redirect:/"));
    }

    @DeleteMapping(BASE_PATH + "/" + FILENAME)
    public Mono<String> deleteFile(@PathVariable String filename) {
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"));
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        Flux<HashMap<String, Object>> map = imageService.findAllImages()
                .flatMap(image -> Mono.just(image)
                        .zipWith(repository.findByImageId(image.getId()).collectList()))
                .log()
                .map(imageAndComments -> new HashMap<String, Object>() {{
                    log.info("List To Map");
                    put("id", imageAndComments.getT1().getId());
                    put("name", imageAndComments.getT1().getName());
                    put("comments", imageAndComments.getT2());
                }})
                ;
        model.addAttribute("images", map);
        model.addAttribute("extra", "DevTools can also detect code changes too");
        return Mono.just("index");
    }
}
