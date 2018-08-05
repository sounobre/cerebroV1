/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerebro.cerebro.util;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

/**
 *
 * @author souno
 */
@Component
public class UploadUtil {
    
    public Supplier<Stream<Row>> getRowStreamSupplier(Iterable<Row> rows) {
        return () -> getStream(rows);
    }

    public <T> Stream<T> getStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
    
    public Supplier<Stream<Integer>> cellIteratorSupplier(int end){
        return () -> numberStream(end);
    }
    
    public Stream<Integer> numberStream(int end){
        return IntStream.range(0, end).boxed();
    }
    
}
