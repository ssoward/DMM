--select * from Products where productName like '%bogus%';

select productSKU, trans.transDate, MURRAY, OREM, LEHI, prods.productCost1, supplierName, (productCost2 * 1) discount,
    if(MURRAY>0, MURRAY * prods.productCost1, null) murrayInv,
    if(OREM>0,   OREM * prods.productCost1, null) oremInv,
    if(LEHI>0,   LEHI * prods.productCost1, null) lehiInv,

    if(productCost2>0 && MURRAY > 0, (MURRAY * prods.productCost1 * (productCost2 * .01)), null) murrayInvWithDiscount,
    if(productCost2>0 && OREM > 0,(OREM   * prods.productCost1 * (productCost2 * .01)), null) oremInvWithDiscount,
    if(productCost2>0 && LEHI > 0,(LEHI   * prods.productCost1 * (productCost2 * .01)), null) lehiInvWithDiscount

    from Transactions trans
        left join 
            Products prods on prods.productNum = trans.productNum
        left join 
            Suppliers sups on sups.supplierNum = prods.productSupplier1
        left join 
            ProductsLocationCount plc on plc.productNum = trans.productNum
        where 
            transType = 'PO'
            and year(transDate) = 2013
    group by trans.productNum, transDate order by productSupplier1;

--select * from Transactions where cast(transShipDate as char) = '0000-00-00 00:00:00';-- is not null-- = '0000-00-00 00:00:00'; -- where (transDate = '0000-00-00 00:00:00' or  transShipDate = '0000-00-00 00:00:00');
--update Transactions set transShipDate = null where cast(transShipDate as char) = '0000-00-00 00:00:00';
--update Transactions set transDate = null where cast(transDate as char) = '0000-00-00 00:00:00';