package com.gpc.salestaxes;

import com.gpc.salestaxes.model.LineItem;
import com.gpc.salestaxes.service.ReadCalculateTaxInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GpcRunner implements CommandLineRunner {

    @Autowired
    private ResourceLoader resourceLoader;


    @Autowired
    private ReadCalculateTaxInput readCalculateTaxInput;

    @Override
    public void run(String... args) throws Exception {

        Resource res = resourceLoader.getResource("classpath:input1.txt");
        printReceipt(res, "output1.txt");

        Resource res2 = resourceLoader.getResource("classpath:input2.txt");
        printReceipt(res2, "output2.txt");

        Resource res3 = resourceLoader.getResource("classpath:input3.txt");
        printReceipt(res3, "output3.txt");


    }

    private void printReceipt(Resource res, String filename) throws IOException {
        List<LineItem> taxedItems =  readCalculateTaxInput.getTaxedItems(res);

        List<LineItem> taxedItemsCalculated = readCalculateTaxInput.calculateTaxedItems(taxedItems);

        for (LineItem lineItem : taxedItems) {
            System.out.println(lineItem);
        }

        readCalculateTaxInput.writeReceipt(taxedItems, filename);
    }
}
