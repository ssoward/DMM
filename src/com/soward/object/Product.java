package com.soward.object;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Product implements Comparable, Serializable{

    public String productNum;
    public String productName;
    public String productAuthor;
    public String productArtist;
    public String productArranger;
    public String productDescription;
    public String category;
    public String productCost1;
    public String productCost2;
    public String productCost3;
    public String productCost4;
    public String productBarCode;
    public String productSKU;
    public String productCatalogNum;
    public String productSupplier1;
    public String productSupplier2;
    public String productSupplier3;
    public String productSupplier4;
    public String numAvailable;
    public String lastSold;
    public String lastInvDate;
    public String lastDODte;
    public String DCCatalogNum;
    public String yearsSold;
    public String oneMonthSold;
    public String sixMonthsSold;
    public String location;
    @JsonIgnore
    public Supplier supplier;
    @JsonIgnore
    public Transaction transaction;
    @JsonIgnore
    public Product lehiProduct;
    private String weight;

    public String getOneMonthSold() {
        return oneMonthSold;
    }

    public void setOneMonthSold(String oneMonthSold) {
        this.oneMonthSold = oneMonthSold;
    }

    public String getSixMonthsSold() {
        return sixMonthsSold;
    }

    public void setSixMonthsSold(String sixMonthsSold) {
        this.sixMonthsSold = sixMonthsSold;
    }

    public String getYearsSold() {
        return yearsSold;
    }

    public void setYearsSold(String setYearsSold) {
        this.yearsSold = setYearsSold;
    }

    /**
     * @return the lastDODte
     */
    public String getLastDODte() {
        return lastDODte;
    }

    /**
     * @param lastDODte the lastDODte to set
     */
    public void setLastDODte(String lastDODte) {
        this.lastDODte = lastDODte;
    }

    public Product getLehiProduct() {
        return lehiProduct;
    }

    public void setLehiProduct( Product lehiProduct ) {
        this.lehiProduct = lehiProduct;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier( Supplier supplier ) {
        this.supplier = supplier;
    }

    /*
     * category consists of:
     * $category = $departmentCode ? $departmentCode : '0';
     * $category .= $event1 ? $event1 : '0';
     * $category .= $event2 ? $event2 : '0';
     * $category .= $event3 ? $event3 : '0';
     * $category .= $event4 ? $event4 : '0';
     * $category .= $descriptionCode ? $descriptionCode : '0';

     */public String getCategory() {
        category = category != null? category: "";
        return category;
    }

    @JsonIgnore
    public void getthis(){
        Calendar now = Calendar.getInstance();
        now.add( Calendar.YEAR, -1 );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format( now );
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public String getDCCatalogNum() {
        DCCatalogNum = DCCatalogNum != null? DCCatalogNum: "";
        return DCCatalogNum;
    }

    public void setDCCatalogNum( String catalogNum ) {
        DCCatalogNum = catalogNum;
    }

    public String getLastInvDate() {
        lastInvDate = lastInvDate != null? lastInvDate: "";
        return lastInvDate;
    }

    public void setLastInvDate( String lastInvDate ) {
        this.lastInvDate = lastInvDate;
    }

    public String getLastSold() {
        lastSold = lastSold != null? lastSold: "";
        return lastSold;
    }

    public void setLastSold( String lastSold ) {
        this.lastSold = lastSold;
    }

    public String getNumAvailable() {
        numAvailable = numAvailable != null? numAvailable: "";
        return numAvailable;
    }

    public void setNumAvailable( String numAvailable ) {
        this.numAvailable = numAvailable;
    }

    public String getProductArranger() {
        productArranger = productArranger != null? productArranger: "";
        return productArranger;
    }

    public void setProductArranger( String productArranger ) {
        this.productArranger = productArranger;
    }

    public String getProductArtist() {
        productArtist = productArtist != null? productArtist: "";
        return productArtist;
    }

    public void setProductArtist( String productArtist ) {
        this.productArtist = productArtist;
    }

    public String getProductAuthor() {
        productAuthor = productAuthor != null? productAuthor: "";
        return productAuthor;
    }

    public void setProductAuthor( String productAuthor ) {
        this.productAuthor = productAuthor;
    }

    public String getProductBarCode() {
        productBarCode = productBarCode != null? productBarCode: "";
        return productBarCode;
    }

    public void setProductBarCode( String productBarCode ) {
        this.productBarCode = productBarCode;
    }

    public String getProductCatalogNum() {
        productCatalogNum = productCatalogNum != null? productCatalogNum: "";
        return productCatalogNum;
    }

    public void setProductCatalogNum( String productCatalogNum ) {
        this.productCatalogNum = productCatalogNum;
    }

    public String getProductCost1() {
        productCost1 = productCost1 != null? productCost1: "";
        try{
            productCost1 = productCost1.substring( 0, productCost1.indexOf( '.' )+3 );
        }catch(Exception e){
        }
        return productCost1;
    }
    public static void main(String args[]){
        String cost = "85.1200";
        System.out.println(cost.substring( 0, cost.indexOf( '.' )+3 ));
    }

    public void setProductCost1( String productCost1 ) {
        this.productCost1 = productCost1;
    }

    public String getProductCost2() {
        productCost2 = productCost2 != null? productCost2: "";
        return productCost2;
    }

    public void setProductCost2( String productCost2 ) {
        this.productCost2 = productCost2;
    }

    public String getProductCost3() {
        productCost3 = productCost3 != null? productCost3: "";
        return productCost3;
    }

    public void setProductCost3( String productCost3 ) {
        this.productCost3 = productCost3;
    }

    public String getProductCost4() {
        productCost4 = productCost4 != null? productCost4: "";
        return productCost4;
    }

    public void setProductCost4( String productCost4 ) {
        this.productCost4 = productCost4;
    }

    public String getProductDescription() {
        productDescription = productDescription != null? productDescription: "";
        return productDescription;
    }

    public void setProductDescription( String productDescription ) {
        this.productDescription = productDescription;
    }

    public String getProductName() {
        productName = productName != null? productName: "";
        return productName;
    }

    public void setProductName( String productName ) {
        this.productName = productName;
    }

    public String getProductSKU() {
        productSKU = productSKU != null? productSKU: "";
        return productSKU;
    }

    public void setProductSKU( String productSKU ) {
        this.productSKU = productSKU;
    }

    public String getProductSupplier1() {
        productSupplier1 = productSupplier1 != null? productSupplier1: "";
        return productSupplier1;
    }

    public void setProductSupplier1( String productSupplier1 ) {
        this.productSupplier1 = productSupplier1;
    }

    public String getProductSupplier2() {
        productSupplier2 = productSupplier2 != null? productSupplier2: "";
        return productSupplier2;
    }

    public void setProductSupplier2( String productSupplier2 ) {
        this.productSupplier2 = productSupplier2;
    }

    public String getProductSupplier3() {
        productSupplier3 = productSupplier3 != null? productSupplier3: "";
        return productSupplier3;
    }

    public void setProductSupplier3( String productSupplier3 ) {
        this.productSupplier3 = productSupplier3;
    }

    public String getProductSupplier4() {
        productSupplier4 = productSupplier4 != null? productSupplier4: "";
        return productSupplier4;
    }

    public void setProductSupplier4( String productSupplier4 ) {
        this.productSupplier4 = productSupplier4;
    }

    public String getProductNum() {
        productNum = productNum != null? productNum: "";
        return productNum;
    }

    public void setProductNum( String productNum ) {
        this.productNum = productNum;
    }

    public String getLocation() {
        location = location != null? location: "";
        return location;
    }

    public void setLocation( String location ) {
        this.location = location;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction( Transaction transaction ) {
        this.transaction = transaction;
    }

    public Product( String productNum, String productName, String productAuthor, String productArtist, String productArranger, String productDescription, String category, String productCost1, String productCost2, String productCost3, String productCost4, String productBarCode, String productSKU, String productCatalogNum, String productSupplier1, String productSupplier2, String productSupplier3, String productSupplier4, String numAvailable, String lastSold, String lastInvDate, String catalogNum, String location, Supplier supplier, Transaction transaction, Product lehiProduct ) {
        this.productNum = productNum;
        this.productName = productName;
        this.productAuthor = productAuthor;
        this.productArtist = productArtist;
        this.productArranger = productArranger;
        this.productDescription = productDescription;
        this.category = category;
        this.productCost1 = productCost1;
        this.productCost2 = productCost2;
        this.productCost3 = productCost3;
        this.productCost4 = productCost4;
        this.productBarCode = productBarCode;
        this.productSKU = productSKU;
        this.productCatalogNum = productCatalogNum;
        this.productSupplier1 = productSupplier1;
        this.productSupplier2 = productSupplier2;
        this.productSupplier3 = productSupplier3;
        this.productSupplier4 = productSupplier4;
        this.numAvailable = numAvailable;
        this.lastSold = lastSold;
        this.lastInvDate = lastInvDate;
        DCCatalogNum = catalogNum;
        this.location = location;
        this.supplier = supplier;
        this.transaction = transaction;
        this.lehiProduct = lehiProduct;
    }
    public Product( ) {
        this.productNum = "";
        this.productName = "";
        this.productAuthor = "";
        this.productArtist = "";
        this.productArranger = "";
        this.productDescription = "";
        this.category = "";
        this.productCost1 = "0.0";
        this.productCost2 = "0.0";
        this.productCost3 = "0.0";
        this.productCost4 = "0.0";
        this.productBarCode = "";
        this.productSKU = "";
        this.productCatalogNum = "";
        this.productSupplier1 = "0";
        this.productSupplier2 = "0";
        this.productSupplier3 = "0";
        this.productSupplier4 = "0";
        this.numAvailable = "0";
        this.lastSold = "";
        this.lastInvDate = "";
        this.DCCatalogNum = "";
        this.location = "";
        this.supplier = new Supplier();
        this.transaction = new Transaction();
    }

    @JsonIgnore
    public String toString(){
        String prod = "";
        prod += " Num:         "+productNum;
        prod += " Name:        "+productName;
        prod += " Author:      "+productAuthor;
        prod += " Artist:      "+productArtist;
        prod += " Arranger:    "+productArranger;
        prod += " Description: "+productDescription;
        prod += " category:    "+category;
        prod += " Cost1:       "+productCost1;
        prod += " Cost2:       "+productCost2;
        prod += " Cost3:       "+productCost3;
        prod += " Cost4:       "+productCost4;
        prod += " BarCode:     "+productBarCode;
        prod += " SKU:         "+productSKU;
        prod += " CatalogNum:  "+productCatalogNum;
        prod += " Supplier1:   "+productSupplier1;
        prod += " Supplier2:   "+productSupplier2;
        prod += " Supplier3:   "+productSupplier3;
        prod += " Supplier4:   "+productSupplier4;
        prod += " numAvailable:"+numAvailable;
        prod += " lastSold:    "+lastSold;
        prod += " lastInvDate: "+lastInvDate;
        prod += " DCCatalogNum:"+DCCatalogNum;
        prod += " location:    "+location;
        prod += "\n ================================================\n";
        return prod;
    }

    @JsonIgnore
    public int compareTo(Object o) {
        Product x = (Product)o;
        Double mT = getProductSupplier1()!=null?Double.parseDouble(getProductSupplier1()):0;
        Double lT = getProductSupplier2()!=null?Double.parseDouble(getProductSupplier2()):0;
        Double oT = getProductSupplier3()!=null?Double.parseDouble(getProductSupplier3()):0;

        Double mX = x.getProductSupplier1()!=null?Double.parseDouble(x.getProductSupplier1()):0;
        Double lX = x.getProductSupplier2()!=null?Double.parseDouble(x.getProductSupplier2()):0;
        Double oX = x.getProductSupplier3()!=null?Double.parseDouble(x.getProductSupplier3()):0;
        return new Double(mX+lX+oX).compareTo(new Double(mT+lT+oT));
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }
}
