package com.gpc.salestaxes.service;

import com.gpc.salestaxes.model.Goods;
import com.gpc.salestaxes.model.LineItem;
import jdk.nashorn.internal.runtime.options.OptionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

@Component
public class ReadCalculateTaxInput {


    public List<LineItem> calculateTaxedItems(List<LineItem> lineItems) throws IOException {
        for (LineItem lineItem : lineItems) {
            if (lineItem.getItemName().contains("book")) { //books are not taxed
                lineItem.setGoodsType(Goods.BOOKS);
                lineItem.setItemTaxRate(BigDecimal.valueOf(0));
                if (lineItem.getItemName().contains("imported")) {
                    lineItem.setItemTaxRate(BigDecimal.valueOf(0.05)); //Additional 5%
                }
            } else if (lineItem.getItemName().contains("chocolate") || lineItem.getItemName().contains("chocolates")) {
                lineItem.setGoodsType(Goods.FOOD);
                lineItem.setItemTaxRate(BigDecimal.valueOf(0)); //food are not taxed
                if (lineItem.getItemName().contains("imported")) {
                    lineItem.setItemTaxRate(BigDecimal.valueOf(0.05)); //Additional 5%
                }
            } else if (lineItem.getItemName().contains("pills")) {
                lineItem.setGoodsType(Goods.MEDICAL);
                lineItem.setItemTaxRate(BigDecimal.valueOf(0)); //food are not taxed
                if (lineItem.getItemName().contains("imported")) {
                    lineItem.setItemTaxRate(BigDecimal.valueOf(0.05)); //Additional 5%
                }
            } else {
                lineItem.setGoodsType(Goods.OTHER);
                lineItem.setItemTaxRate(BigDecimal.valueOf(0.1));
                //TODO These requirements does not explicitly state how the extra 5% import duty
                //should be applied
                if (lineItem.getItemName().contains("imported")) {
//                    BigDecimal lineItemSalesTax;
//                    BigDecimal lineItemAmountWithTax;
//                    lineItemSalesTax = lineItem.getItemPrice().multiply(lineItem.getItemTaxRate());
//                    lineItemAmountWithTax = lineItem.getItemPrice().add(lineItemSalesTax);
//                    lineItem.setItemPrice(lineItemAmountWithTax);

                    lineItem.setItemTaxRate(BigDecimal.valueOf(0.15)); //Additional 5%
                }
            }


        }
        return lineItems;
    }

    public void writeReceipt(List<LineItem> lineItems, String fileName) throws IOException {

        final String FILE_NAME = "src/main/resources/" + fileName;
        //ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(FILE_NAME);
        if (file.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }

        //FileWriter fileWriter = new FileWriter(file);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        BigDecimal salesTaxTotal = BigDecimal.valueOf(0);
        BigDecimal payableAmountTotal = BigDecimal.valueOf(0);

        for (LineItem lineItem : lineItems) {
            BigDecimal lineItemSalesTax;
            BigDecimal lineItemAmountWithTax;
            lineItemSalesTax = lineItem.getItemPrice().multiply(lineItem.getItemTaxRate());

            BigDecimal roundingResult = new BigDecimal(Math.ceil(lineItemSalesTax.doubleValue() * 20) / 20);
            roundingResult.setScale(2, RoundingMode.HALF_UP);
            lineItemSalesTax = roundingResult;
            lineItemSalesTax = lineItemSalesTax.setScale(2, BigDecimal.ROUND_HALF_UP);

            salesTaxTotal = salesTaxTotal.add(lineItemSalesTax);

            lineItemAmountWithTax = lineItem.getItemPrice().add(lineItemSalesTax);
            lineItemAmountWithTax = lineItemAmountWithTax.setScale(2, BigDecimal.ROUND_HALF_UP);
            payableAmountTotal = payableAmountTotal.add(lineItemAmountWithTax);
            String str = lineItem.getCount() + " " + lineItem.getItemName() + " : " + lineItemAmountWithTax;

            writer.write(str);
            writer.newLine();

        }
        writer.write("Sales Taxes: " + salesTaxTotal);
        writer.newLine();
        writer.write("Total: " + payableAmountTotal);
        writer.newLine();

        writer.close();


    }

    public List<LineItem> getTaxedItems(Resource res) throws IOException {


        List<LineItem> items = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(res.getURI()),
                StandardCharsets.UTF_8);

        for (String line : lines) {
            LineItem lineItem = new LineItem();

            String[] strings = line.split("\\s+");
            int pos = 0;
            lineItem.setCount(Integer.parseInt(strings[pos]));
            String itemName = "";
            int nameCount = 0;
            for (pos = 1; pos < strings.length - 1; pos++) {
                if (strings[pos].equalsIgnoreCase("at")) {
                    break;
                } else {
                    nameCount++;
                    if (nameCount > 1) {
                        itemName = itemName + " ";
                    }
                    itemName = itemName + strings[pos];
                    lineItem.setItemName(itemName);
                }
            }
            double itemPrice = parseDouble(strings[strings.length - 1]);
            lineItem.setItemPrice(BigDecimal.valueOf(itemPrice));
            items.add(lineItem);
        }

        return items;
    }

}
