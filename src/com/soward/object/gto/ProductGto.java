package com.soward.object.gto;

import com.soward.object.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 1/10/12
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductGto implements Comparable{

    private List<Product> prodList;
    private Double totalValue;
    private Double mTotal;
    private Double lTotal;
    private Double oTotal;
    private String name;

    public ProductGto(){
        this.prodList = new ArrayList<Product>();
        this.mTotal = new Double(0);
        this.lTotal = new Double(0);
        this.oTotal = new Double(0);
        this.totalValue = new Double(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getmTotal() {
        return mTotal;
    }

    public void setmTotal(Double mTotal) {
        this.mTotal = mTotal;
    }

    public Double getlTotal() {
        return lTotal;
    }

    public void setlTotal(Double lTotal) {
        this.lTotal = lTotal;
    }

    public Double getoTotal() {
        return oTotal;
    }

    public void setoTotal(Double oTotal) {
        this.oTotal = oTotal;
    }

    public List<Product> getProdList() {
        Collections.sort(prodList);
        return prodList;
    }

    public void setProdList(List<Product> prodList) {
        this.prodList = prodList;
    }

    public Double getTotalValue() {
        return mTotal+lTotal+oTotal;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public int compareTo(Object o) {
        if(this.getTotalValue()!=null&&((ProductGto)o).getTotalValue()!=null&&getTotalValue()==((ProductGto)o).getTotalValue()){
            return 0;
        }
        if(this.getTotalValue()!=null&&getTotalValue()>((ProductGto)o).getTotalValue()){
            return 1;
        }
        else{
            return -1;
        }
    }
}
