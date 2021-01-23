package com.example.smartboldriver.models

import java.util.*

 class DeliveryObject {
    private var sbolnum: String? = null
    private var txdate: String? = null
    private var shipdate: String? = null
    private var srclocation: String? = null
    private var doccode: String? = null
    private var picknum: String? = null
    private var delvnum: String? = null
    private var bolnum: String? = null
    private var shipnum: String? = null
    private var shipper: String? = null
    private var shipadd1: String? = null
    private var shipadd2: String? = null
    private var shipcity: String? = null
    private var shipstate: String? = null
    private var shipzip: String? = null
    private var shipcntry: String? = null
    private var shipacc: String? = null
    private var consignee: String? = null
    private var consadd1: String? = null
    private var consadd2: String? = null
    private var conscity: String? = null
    private var consstate: String? = null
    private var conszip: String? = null
    private var conscntry: String? = null
    private var consattn: String? = null
    private var consacc: String? = null
    private var hazmat: String? = null
    private var hmcontact: String? = null
    private var spinstr: String? = null
    private var stopnum: String? = null
    private var status: String? = null
    private var downloaddate: String? = null
    private var token: String? = null
    private var path: String? = null
    private var pronum: String? = null
    private var comments: String? = null
    private var pickup: String? = null

    private var selected = false
    lateinit var deliveryList:  ArrayList<DeliveryObject>


    fun DeliveryObject(
        sbolnum: String?,
        txdate: String?,
        shipdate: String?,
        srclocation: String?,
        doccode: String?,
        picknum: String?,
        delvnum: String?,
        bolnum: String?,
        shipnum: String?,
        shipper: String?,
        shipadd1: String?,
        shipadd2: String?,
        shipcity: String?,
        shipstate: String?,
        shipzip: String?,
        shipcntry: String?,
        shipacc: String?,
        consignee: String?,
        consadd1: String?,
        consadd2: String?,
        conscity: String?,
        consstate: String?,
        conszip: String?,
        conscntry: String?,
        consattn: String?,
        consacc: String?,
        hazmat: String?,
        hmcontact: String?,
        spinstr: String?,
        stopnum: String?,
        status: String?,
        downloaddate: String?,
        token: String?,
        path: String?,
        pronum: String?,
        comments: String?,
        pickup: String?
    ) {
        this.sbolnum = sbolnum
        this.txdate = txdate
        this.shipdate = shipdate
        this.srclocation = srclocation
        this.doccode = doccode
        this.picknum = picknum
        this.delvnum = delvnum
        this.bolnum = bolnum
        this.shipnum = shipnum
        this.shipper = shipper
        this.shipadd1 = shipadd1
        this.shipadd2 = shipadd2
        this.shipcity = shipcity
        this.shipstate = shipstate
        this.shipzip = shipzip
        this.shipcntry = shipcntry
        this.shipacc = shipacc
        this.consignee = consignee
        this.consadd1 = consadd1
        this.consadd2 = consadd2
        this.conscity = conscity
        this.consstate = consstate
        this.conszip = conszip
        this.conscntry = conscntry
        this.consattn = consattn
        this.consacc = consacc
        this.hazmat = hazmat
        this.hmcontact = hmcontact
        this.spinstr = spinstr
        this.stopnum = stopnum
        this.status = status
        this.downloaddate = downloaddate
        this.token = token
        this.path = path
        this.pronum = pronum
        this.comments = comments
        this.pickup = pickup
    }

    fun addToDeliveryList(list: DeliveryObject?) {
        val d_list = deliveryList.add(list!!)
    }

    fun getDeliveries(): List<DeliveryObject?>? {
        return deliveryList
    }

    fun getSrclocation(): String? {
        return srclocation
    }

    fun setSrclocation(srclocation: String?) {
        this.srclocation = srclocation
    }

    fun getDoccode(): String? {
        return doccode
    }

    fun setDoccode(doccode: String?) {
        this.doccode = doccode
    }

    fun getSbolnum(): String? {
        return sbolnum
    }

    fun setSbolnum(sbolnum: String?) {
        this.sbolnum = sbolnum
    }

    fun getShipnum(): String? {
        return shipnum
    }

    fun setShipnum(shipnum: String?) {
        this.shipnum = shipnum
    }

    fun getShipper(): String? {
        return shipper
    }

    fun setShipper(shipper: String?) {
        this.shipper = shipper
    }

    fun getShipadd1(): String? {
        return shipadd1
    }

    fun setShipadd1(shipadd1: String?) {
        this.shipadd1 = shipadd1
    }

    fun getShipadd2(): String? {
        return shipadd2
    }

    fun setShipadd2(shipadd2: String?) {
        this.shipadd2 = shipadd2
    }

    fun getShipcity(): String? {
        return shipcity
    }

    fun setShipcity(shipcity: String?) {
        this.shipcity = shipcity
    }

    fun getShipstate(): String? {
        return shipstate
    }

    fun setShipstate(shipstate: String?) {
        this.shipstate = shipstate
    }

    fun getShipzip(): String? {
        return shipzip
    }

    fun setShipzip(shipzip: String?) {
        this.shipzip = shipzip
    }

    fun getShipcntry(): String? {
        return shipcntry
    }

    fun setShipcntry(shipcntry: String?) {
        this.shipcntry = shipcntry
    }

    fun getShipacc(): String? {
        return shipacc
    }

    fun setShipacc(shipacc: String?) {
        this.shipacc = shipacc
    }

    fun getConsadd1(): String? {
        return consadd1
    }

    fun setConsadd1(consadd1: String?) {
        this.consadd1 = consadd1
    }

    fun getConsignee(): String? {
        return consignee
    }

    fun setConsignee(consignee: String?) {
        this.consignee = consignee
    }

    fun getConscity(): String? {
        return conscity
    }

    fun setConscity(conscity: String?) {
        this.conscity = conscity
    }

    fun getConsadd2(): String? {
        return consadd2
    }

    fun setConsadd2(consadd2: String?) {
        this.consadd2 = consadd2
    }

    fun getConsstate(): String? {
        return consstate
    }

    fun setConsstate(consstate: String?) {
        this.consstate = consstate
    }

    fun getConszip(): String? {
        return conszip
    }

    fun setConszip(conszip: String?) {
        this.conszip = conszip
    }

    fun getConscntry(): String? {
        return conscntry
    }

    fun setConscntry(conscntry: String?) {
        this.conscntry = conscntry
    }

    fun getConsattn(): String? {
        return consattn
    }

    fun setConsattn(consattn: String?) {
        this.consattn = consattn
    }

    fun getConsacc(): String? {
        return consacc
    }

    fun setConsacc(consacc: String?) {
        this.consacc = consacc
    }

    fun getHazmat(): String? {
        return hazmat
    }

    fun setHazmat(hazmat: String?) {
        this.hazmat = hazmat
    }

    fun getHmcontact(): String? {
        return hmcontact
    }

    fun setHmcontact(hmcontact: String?) {
        this.hmcontact = hmcontact
    }

    fun getSpinstr(): String? {
        return spinstr
    }

    fun setSpinstr(spinstr: String?) {
        this.spinstr = spinstr
    }

    fun getStopnum(): String? {
        return stopnum
    }

    fun setStopnum(stopnum: String?) {
        this.stopnum = stopnum
    }

    fun getBolnum(): String? {
        return bolnum
    }

    fun setBolnum(bolnum: String?) {
        this.bolnum = bolnum
    }

    fun getShipdate(): String? {
        return shipdate
    }

    fun setShipdate(shipdate: String?) {
        this.shipdate = shipdate
    }

    fun getTxdate(): String? {
        return txdate
    }

    fun setTxdate(txdate: String?) {
        this.txdate = txdate
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }


    fun isSelected(): Boolean {
        return selected
    }

    fun setSelected(selected: Boolean) {
        this.selected = selected
    }


    fun getPicknum(): String? {
        return picknum
    }

    fun setPicknum(picknum: String?) {
        this.picknum = picknum
    }

    fun getDelvnum(): String? {
        return delvnum
    }

    fun setDelvnum(delvnum: String?) {
        this.delvnum = delvnum
    }

    fun getPath(): String? {
        return path
    }

    fun setPath(path: String?) {
        this.path = path
    }

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String?) {
        this.token = token
    }

    fun getDownloaddate(): String? {
        return downloaddate
    }

    fun setDownloaddate(downloaddate: String?) {
        this.downloaddate = downloaddate
    }

    fun getPronum(): String? {
        return pronum
    }

    fun setPronum(pronum: String?) {
        this.pronum = pronum
    }

    fun getComments(): String? {
        return comments
    }

    fun setComments(comments: String?) {
        this.comments = comments
    }

    fun getPickup(): String? {
        return pickup
    }

    fun setPickup(pickup: String?) {
        this.pickup = pickup
    }
}