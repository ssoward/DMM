angular.module('dmm-core').service('InvoiceTransService', function ($http, $log, $state, $rootScope, $q) {

    this.getTranscations = function (invoiceNum){
        return $http({
            method: 'GET',
            url: './invoices',
            params: {
                invoiceNum: invoiceNum,
                funct: 'TRANS_GET'
            }
        });
    };

    this.addProducttoPOrder = function (invoiceNum, productNum){
        return $http({
            method: 'PUT',
            url: './invoices',
            params: {
                funct: 'TRANS_PUT',
                invoiceNum:invoiceNum,
                productNum:productNum
            }
        });
    };

    this.saveProductQty = function (transNum, productQty){
        return $http({
            method: 'PUT',
            url: './invoices',
            params: {
                funct: 'TRANS_QTY_PUT',
                transNum:transNum,
                productQty:productQty
            }
        });
    };

    this.deleteTranscation = function (transNum){
        return $http({
            method: 'DELETE',
            url: './invoices',
            params: {
                funct: 'TRANS_DELETE',
                transNum:transNum
            }
        });
    };

    this.deleteInvoice = function (invoiceNum){
        return $http({
            method: 'DELETE',
            url: './invoices',
            params: {
                funct: 'INV_DELETE',
                invoiceNum:invoiceNum
            }
        });
    };

    //var res = {'status': 200,'config':{},'data':
    //    [{"invoiceNum":"714851","accountNum":"102","invoiceDate":"2015-02-18 09:54:11","locationNum":"20","username2":"kelsey","invoiceTotal":"15.3700","invoiceTax":"0.9720","invoiceShipTotal":"0.0000","invoicePaid":"20.3700","paymentMethod1":"100","paymentMethod2":null,"invoicePaid1":"20.3700","invoicePaid2":"0.0000","invoiceReceivedBy":null,"invoiceContactNum":null,"invoiceReferenceNum":null,"invoiceChargeStatus":null,"invoiceChargeDate":null,"invoiceChargePaymentMethod":null,"invoiceDiscount":"0.0000","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[{"transNum":"2179781","invoiceNum":"714851","transType":"NS","productNum":"194810","productName":"3 VANDOREN BASS CLARINET REED","productQty":"2","transCost":"15.3720","transTax":"0.9720","transProductStatus":"SOLD","transDate":"2015-02-18 09:54:11","locationNum":"20","username":"kelsey","transShipCost":"0.0000","transShipped":"0","transShipDate":null,"account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"invoice":{"invoiceNum":"","accountNum":"","invoiceDate":"","locationNum":"","username2":"","invoiceTotal":"","invoiceTax":"","invoiceShipTotal":"","invoicePaid":"","paymentMethod1":"","paymentMethod2":"","invoicePaid1":"","invoicePaid2":"","invoiceReceivedBy":"","invoiceContactNum":"","invoiceReferenceNum":"","invoiceChargeStatus":"","invoiceChargeDate":"","invoiceChargePaymentMethod":"","invoiceDiscount":"","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},"prod":{"productNum":"194810","productName":"3 VANDOREN BASS CLARINET REED","productAuthor":"","productArtist":"","productArranger":"","productDescription":"","category":"800008","productCost1":"7.20","productCost2":"0.0000","productCost3":"0.0000","productCost4":"0.0000","productBarCode":"008576110260","productSKU":"DMM194810","productCatalogNum":"CR123","productSupplier1":"565","productSupplier2":"0","productSupplier3":"0","productSupplier4":"0","numAvailable":"5","lastSold":"2015-02-19 15:56:57","lastInvDate":"2014-10-18 16:26:42","lastDODte":null,"DCCatalogNum":null,"yearsSold":null,"oneMonthSold":null,"sixMonthsSold":null,"location":"","weight":null,"dccatalogNum":""},"transDateFormatted":"02-18-2015","transYear":"8"}],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},{"invoiceNum":"714852","accountNum":"101","invoiceDate":"2015-02-18 10:21:52","locationNum":"7","username2":"jd","invoiceTotal":"10.6300","invoiceTax":"0.6816","invoiceShipTotal":"0.0000","invoicePaid":"10.6300","paymentMethod1":"97","paymentMethod2":null,"invoicePaid1":"10.6300","invoicePaid2":"0.0000","invoiceReceivedBy":null,"invoiceContactNum":null,"invoiceReferenceNum":"689226","invoiceChargeStatus":null,"invoiceChargeDate":null,"invoiceChargePaymentMethod":null,"invoiceDiscount":"0.0000","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[{"transNum":"2179782","invoiceNum":"714852","transType":"NS","productNum":"206962","productName":"BATON MISC.","productQty":"1","transCost":"10.6316","transTax":"0.6816","transProductStatus":"SOLD","transDate":"2015-02-18 10:21:52","locationNum":"7","username":"jd","transShipCost":"0.0000","transShipped":"0","transShipDate":null,"account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"invoice":{"invoiceNum":"","accountNum":"","invoiceDate":"","locationNum":"","username2":"","invoiceTotal":"","invoiceTax":"","invoiceShipTotal":"","invoicePaid":"","paymentMethod1":"","paymentMethod2":"","invoicePaid1":"","invoicePaid2":"","invoiceReceivedBy":"","invoiceContactNum":"","invoiceReferenceNum":"","invoiceChargeStatus":"","invoiceChargeDate":"","invoiceChargePaymentMethod":"","invoiceDiscount":"","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},"prod":{"productNum":"206962","productName":"BATON MISC.","productAuthor":"","productArtist":"","productArranger":"","productDescription":"IF NOT PRICED THEY ARE 9.95","category":"900000","productCost1":"9.95","productCost2":"0.0000","productCost3":"0.0000","productCost4":"0.0000","productBarCode":"","productSKU":"DMM206962","productCatalogNum":"3870","productSupplier1":"565","productSupplier2":"0","productSupplier3":"0","productSupplier4":"0","numAvailable":"22","lastSold":"2015-02-23 15:11:48","lastInvDate":"2014-11-24 14:10:04","lastDODte":null,"DCCatalogNum":null,"yearsSold":null,"oneMonthSold":null,"sixMonthsSold":null,"location":"","weight":null,"dccatalogNum":""},"transDateFormatted":"02-18-2015","transYear":"8"}],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},{"invoiceNum":"714853","accountNum":"101","invoiceDate":"2015-02-18 10:28:21","locationNum":"5","username2":"jb","invoiceTotal":"39.4700","invoiceTax":"2.5304","invoiceShipTotal":"0.0000","invoicePaid":"39.4700","paymentMethod1":"98","paymentMethod2":null,"invoicePaid1":"39.4700","invoicePaid2":"0.0000","invoiceReceivedBy":null,"invoiceContactNum":null,"invoiceReferenceNum":"4134","invoiceChargeStatus":null,"invoiceChargeDate":null,"invoiceChargePaymentMethod":null,"invoiceDiscount":"0.0000","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[{"transNum":"2179783","invoiceNum":"714853","transType":"NS","productNum":"198251","productName":"MOST REQUESTED SONGS OF THE '80S","productQty":"1","transCost":"21.3593","transTax":"1.3693","transProductStatus":"SOLD","transDate":"2015-02-18 10:28:21","locationNum":"5","username":"jb","transShipCost":"0.0000","transShipped":"0","transShipDate":null,"account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"invoice":{"invoiceNum":"","accountNum":"","invoiceDate":"","locationNum":"","username2":"","invoiceTotal":"","invoiceTax":"","invoiceShipTotal":"","invoicePaid":"","paymentMethod1":"","paymentMethod2":"","invoicePaid1":"","invoicePaid2":"","invoiceReceivedBy":"","invoiceContactNum":"","invoiceReferenceNum":"","invoiceChargeStatus":"","invoiceChargeDate":"","invoiceChargePaymentMethod":"","invoiceDiscount":"","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},"prod":{"productNum":"198251","productName":"MOST REQUESTED SONGS OF THE '80S","productAuthor":"","productArtist":"","productArranger":"","productDescription":"PIANO/VOCAL/GUITAR","category":"1bNn06","productCost1":"19.99","productCost2":"50.0000","productCost3":"0.0000","productCost4":"0.0000","productBarCode":"884088864873","productSKU":"DMM198251","productCatalogNum":"00111668","productSupplier1":"128","productSupplier2":"0","productSupplier3":"0","productSupplier4":"0","numAvailable":"0","lastSold":"2015-02-18 10:28:21","lastInvDate":"2013-01-30 03:06:00","lastDODte":null,"DCCatalogNum":null,"yearsSold":null,"oneMonthSold":null,"sixMonthsSold":null,"location":"","weight":null,"dccatalogNum":""},"transDateFormatted":"02-18-2015","transYear":"8"},{"transNum":"2179784","invoiceNum":"714853","transType":"NS","productNum":"146865","productName":"100 YEARS OF POPULAR MUSIC- 1980","productQty":"1","transCost":"18.1111","transTax":"1.1611","transProductStatus":"SOLD","transDate":"2015-02-18 10:28:21","locationNum":"5","username":"jb","transShipCost":"0.0000","transShipped":"0","transShipDate":null,"account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"invoice":{"invoiceNum":"","accountNum":"","invoiceDate":"","locationNum":"","username2":"","invoiceTotal":"","invoiceTax":"","invoiceShipTotal":"","invoicePaid":"","paymentMethod1":"","paymentMethod2":"","invoicePaid1":"","invoicePaid2":"","invoiceReceivedBy":"","invoiceContactNum":"","invoiceReferenceNum":"","invoiceChargeStatus":"","invoiceChargeDate":"","invoiceChargePaymentMethod":"","invoiceDiscount":"","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},"prod":{"productNum":"146865","productName":"100 YEARS OF POPULAR MUSIC- 1980","productAuthor":"VARIOUS","productArtist":"VARIOUS","productArranger":"","productDescription":"PIANO/VOCAL/CHORDS","category":"1bNn06","productCost1":"16.95","productCost2":"50.0000","productCost3":"","productCost4":"","productBarCode":"884088679972","productSKU":"DMM146865","productCatalogNum":"00321487","productSupplier1":"128","productSupplier2":"","productSupplier3":"","productSupplier4":"","numAvailable":"0","lastSold":"2015-02-18 10:28:21","lastInvDate":"2012-10-25 12:41:00","lastDODte":null,"DCCatalogNum":null,"yearsSold":null,"oneMonthSold":null,"sixMonthsSold":null,"location":"","weight":null,"dccatalogNum":""},"transDateFormatted":"02-18-2015","transYear":"8"}],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},{"invoiceNum":"714850","accountNum":"101","invoiceDate":"2015-02-18 09:43:34","locationNum":"5","username2":"laurel","invoiceTotal":"11.1600","invoiceTax":"0.7151","invoiceShipTotal":"0.0000","invoicePaid":"11.1600","paymentMethod1":"97","paymentMethod2":null,"invoicePaid1":"11.1600","invoicePaid2":"0.0000","invoiceReceivedBy":null,"invoiceContactNum":null,"invoiceReferenceNum":"018848","invoiceChargeStatus":null,"invoiceChargeDate":null,"invoiceChargePaymentMethod":null,"invoiceDiscount":"0.0000","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[{"transNum":"2179778","invoiceNum":"714850","transType":"NS","productNum":"139447","productName":"MINIATURES BOOK 1","productQty":"1","transCost":"5.2891","transTax":"0.3391","transProductStatus":"SOLD","transDate":"2015-02-18 09:43:34","locationNum":"5","username":"laurel","transShipCost":"0.0000","transShipped":"0","transShipDate":null,"account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"invoice":{"invoiceNum":"","accountNum":"","invoiceDate":"","locationNum":"","username2":"","invoiceTotal":"","invoiceTax":"","invoiceShipTotal":"","invoicePaid":"","paymentMethod1":"","paymentMethod2":"","invoicePaid1":"","invoicePaid2":"","invoiceReceivedBy":"","invoiceContactNum":"","invoiceReferenceNum":"","invoiceChargeStatus":"","invoiceChargeDate":"","invoiceChargePaymentMethod":"","invoiceDiscount":"","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},"prod":{"productNum":"139447","productName":"MINIATURES BOOK 1","productAuthor":"EDWIN MCLEAN","productArtist":"","productArranger":"","productDescription":"ELEMENTARY PIANO","category":"1qKn04","productCost1":"4.95","productCost2":"40.0000","productCost3":"","productCost4":"","productBarCode":"674398210459","productSKU":"DMM139447","productCatalogNum":"FJH1318","productSupplier1":"108","productSupplier2":"","productSupplier3":"","productSupplier4":"","numAvailable":"0","lastSold":"2015-02-18 09:43:34","lastInvDate":"2013-10-01 15:40:00","lastDODte":null,"DCCatalogNum":"","yearsSold":null,"oneMonthSold":null,"sixMonthsSold":null,"location":"","weight":null,"dccatalogNum":""},"transDateFormatted":"02-18-2015","transYear":"8"},{"transNum":"2179779","invoiceNum":"714850","transType":"NS","productNum":"7142","productName":"CIRCUS MARCH","productQty":"1","transCost":"2.6713","transTax":"0.1713","transProductStatus":"SOLD","transDate":"2015-02-18 09:43:34","locationNum":"5","username":"laurel","transShipCost":"0.0000","transShipped":"0","transShipDate":null,"account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"invoice":{"invoiceNum":"","accountNum":"","invoiceDate":"","locationNum":"","username2":"","invoiceTotal":"","invoiceTax":"","invoiceShipTotal":"","invoicePaid":"","paymentMethod1":"","paymentMethod2":"","invoicePaid1":"","invoicePaid2":"","invoiceReceivedBy":"","invoiceContactNum":"","invoiceReferenceNum":"","invoiceChargeStatus":"","invoiceChargeDate":"","invoiceChargePaymentMethod":"","invoiceDiscount":"","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},"prod":{"productNum":"7142","productName":"CIRCUS MARCH","productAuthor":"TINGLEY, GEORGE PETE","productArtist":"","productArranger":"","productDescription":"ELEMENTARY PIANO SOLO","category":"1q0003","productCost1":"2.50","productCost2":"50.0000","productCost3":"0.0000","productCost4":"0.0000","productBarCode":"038081129730","productSKU":"DMM7142","productCatalogNum":"14719","productSupplier1":"27","productSupplier2":"0","productSupplier3":"0","productSupplier4":"0","numAvailable":"0","lastSold":"2015-02-18 09:43:34","lastInvDate":"2010-11-26 10:21:00","lastDODte":null,"DCCatalogNum":"","yearsSold":null,"oneMonthSold":null,"sixMonthsSold":null,"location":"","weight":null,"dccatalogNum":""},"transDateFormatted":"02-18-2015","transYear":"8"},{"transNum":"2179780","invoiceNum":"714850","transType":"NS","productNum":"6758","productName":"CURIOUS KITTENS","productQty":"1","transCost":"3.1948","transTax":"0.2048","transProductStatus":"SOLD","transDate":"2015-02-18 09:43:34","locationNum":"5","username":"laurel","transShipCost":"0.0000","transShipped":"0","transShipDate":null,"account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"invoice":{"invoiceNum":"","accountNum":"","invoiceDate":"","locationNum":"","username2":"","invoiceTotal":"","invoiceTax":"","invoiceShipTotal":"","invoicePaid":"","paymentMethod1":"","paymentMethod2":"","invoicePaid1":"","invoicePaid2":"","invoiceReceivedBy":"","invoiceContactNum":"","invoiceReferenceNum":"","invoiceChargeStatus":"","invoiceChargeDate":"","invoiceChargePaymentMethod":"","invoiceDiscount":"","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},"prod":{"productNum":"6758","productName":"CURIOUS KITTENS","productAuthor":"JUNE C. MONTGOMERY","productArtist":"","productArranger":"","productDescription":"ELEMENTARY PIANO SOLO","category":"100003","productCost1":"2.99","productCost2":"50.0000","productCost3":"0.0000","productCost4":"0.0000","productBarCode":"038081117676","productSKU":"DMM6758","productCatalogNum":"14203","productSupplier1":"27","productSupplier2":"0","productSupplier3":"0","productSupplier4":"0","numAvailable":"1","lastSold":"2015-02-18 09:43:34","lastInvDate":"2015-01-24 11:10:56","lastDODte":null,"DCCatalogNum":"","yearsSold":null,"oneMonthSold":null,"sixMonthsSold":null,"location":"","weight":null,"dccatalogNum":""},"transDateFormatted":"02-18-2015","transYear":"8"}],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null}]
    //};

    this.getSales = function (date, location){

        //var defer = $q.defer();
        //defer.resolve(res);
        //return defer.promise;

        return $http({
            method: 'GET',
            url: './invoices',
            params: {
                date: date,
                location: location,
                funct: 'SALES_GET'
            }
        });
    }

    this.getProductSoldHistory = function (date, location){

        //var defer = $q.defer();
        //defer.resolve(res);
        //return defer.promise;

        return $http({
            method: 'GET',
            url: './invoices',
            params: {
                date: date,
                location: location,
                funct: 'PROD_SOLD_FOR_INVOICES'
            }
        });
    }


});

