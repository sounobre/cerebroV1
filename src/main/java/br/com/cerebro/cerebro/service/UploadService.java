/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerebro.cerebro.service;

import br.com.cerebro.cerebro.model.EntradaChamados;
import br.com.cerebro.cerebro.model.EntradaDupla;
import br.com.cerebro.cerebro.repository.EntradaChamadosRepository;
import br.com.cerebro.cerebro.repository.EntradaDuplaRepository;
import br.com.cerebro.cerebro.util.UploadUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author souno
 */
@Service
public class UploadService {

    private final UploadUtil uploadUtil;

    public UploadService(UploadUtil uploadUtil) {
        this.uploadUtil = uploadUtil;
    }

    @Autowired
    EntradaChamadosRepository entradaChamadosRepository;

    @Autowired
    EntradaDuplaRepository entradaDuplaRepository;

    EntradaChamados entradaChamados = new EntradaChamados();

    EntradaDupla entradaDupla = new EntradaDupla();

    Integer chamado;

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

        return rowStreamSupplier.get()
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

    public void upload1(MultipartFile file) throws Exception {
        Path tempDir = Files.createTempDirectory("");

        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();

        file.transferTo(tempFile);

        Workbook workbook = WorkbookFactory.create(tempFile);

        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rows = sheet.rowIterator();

        DataFormatter df = new DataFormatter();

        while (rows.hasNext()) {
            Row row = rows.next();

            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                Integer rowNumber = row.getRowNum();
                if (sheet.getRow(rowNumber).getCell(0).getCellTypeEnum() == CellType.STRING) {

                } else {
                    chamado = (int) sheet.getRow(rowNumber).getCell(0).getNumericCellValue();
                }
                if (df.formatCellValue(cell).equalsIgnoreCase("chamado")
                        || df.formatCellValue(cell).equalsIgnoreCase("data")
                        || df.formatCellValue(cell).equalsIgnoreCase("executante")) {

                } else {
                    if ((entradaChamadosRepository.findById(chamado).isPresent())
                            && cell.getColumnIndex() == 0) {

                        if (entradaChamadosRepository.findById(chamado).isPresent()) {

                            entradaDupla(chamado, cell, cellIterator);
                            cellIterator.next();
                            break;
                        } else {

                            if (chamado.equals((int) sheet.getRow(rowNumber).getCell(0).getNumericCellValue())
                                    && entradaChamadosRepository.findById(chamado).isPresent()) {
                                entradaDupla(chamado, cell, cellIterator);
                                cellIterator.next();
                                break;

                            }
                            if (entradaChamadosRepository.findById(chamado).isPresent()) {

                                switch (cell.getColumnIndex() + 1) {

                                    case 1: //System.out.println(df.formatCellValue(cell) + " ");
                                        entradaChamados.setChamado((int) cell.getNumericCellValue());
                                        break;

                                    case 2: //System.out.println(cell.getStringCellValue() + " ");
                                        entradaChamados.setData(cell.getStringCellValue());
                                        break;

                                    case 3: //System.out.println(cell.getStringCellValue() + " ");
                                        entradaChamados.setExecutante(cell.getStringCellValue());
                                        break;
                                }
                                entradaChamadosRepository.save(entradaChamados);

                            }
                        }

                    }

                }

            }

        }
    }

    public void entradaDupla(Integer chamado, Cell cell, Iterator<Cell> cellIterator) throws Exception {

        switch (cell.getColumnIndex() + 1) {

            case 1: //System.out.println(df.formatCellValue(cell) + " ");
                entradaDupla.setChamado((int) cell.getNumericCellValue());
                break;

            case 2: //System.out.println(cell.getStringCellValue() + " ");
                entradaDupla.setData(cell.getStringCellValue());
                break;

            case 3: //System.out.println(cell.getStringCellValue() + " ");
                entradaDupla.setExecutante(cell.getStringCellValue());
                break;
        }
        entradaDuplaRepository.save(entradaDupla);

    }

}
