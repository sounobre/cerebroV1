/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerebro.cerebro.service;

import br.com.cerebro.cerebro.util.UploadUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author souno
 */
@Service
public class UploadService {
    
    private final UploadUtil uploadUtil;
    
    public UploadService(UploadUtil uploadUtil){
        this.uploadUtil = uploadUtil;
    }
    
    public List<Map<String, String>> upload(MultipartFile file) throws Exception {

        Path tempDir = Files.createTempDirectory("");

        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
         
        file.transferTo(tempFile);
        
        Workbook workbook = WorkbookFactory.create(tempFile);

        Sheet sheet = workbook.getSheetAt(0);

        Supplier<Stream<Row>> rowStreamSupplier = uploadUtil.getRowStreamSupplier(sheet);
        
        Row headerRow = rowStreamSupplier.get().findFirst().get();
        
        List<String> headerCells = uploadUtil.getStream(headerRow)
                .map(Cell::getStringCellValue)
                .collect(Collectors.toList());
        
        int colCount = headerCells.size();
        
      return  rowStreamSupplier.get()
              .skip(1)
              .map(row -> {
            
           List<String> cellList = StreamSupport.stream(row.spliterator(), false)
                   .map(Cell::getStringCellValue)
                   .collect(Collectors.toList());
            
            return uploadUtil.cellIteratorSupplier(colCount)
                    .get()
                    .collect(toMap(headerCells::get, cellList::get));
            
           
        })
                .collect(Collectors.toList());
    }

}
