package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.payload.FileInfoResponse;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/files")
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping("/{type}")
    public FileInfoResponse getUploadFileRequest(@AuthenticationPrincipal UserPrincipal currentUser,
                                                 @PathVariable("type") String type) throws IOException {
        return fileService.getUploadFileRequest(currentUser, type);
    }

}
