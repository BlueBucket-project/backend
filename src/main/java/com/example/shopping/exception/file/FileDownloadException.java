package com.example.shopping.exception.file;
/*
 *   writer : 유요한
 *   work :
 *          파일 다운로드 예외처리
 *   date : 2023/10/12
 * */
public class FileDownloadException extends RuntimeException{
    public FileDownloadException(String message) {
        super(message);
    }
}
