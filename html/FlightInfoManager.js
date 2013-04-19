var FlightInfoManager = function() {
    FlightInfoManager.superclass.constructor.call(this);
};
$jex.extendClass(FlightInfoManager, InfoManager);
FlightInfoManager.prototype.addFlightInfoSource = function(b, a) {
    this.addSource("flightInfo", b, a);
};
FlightInfoManager.prototype.updateFlightInfoSource = function(c, b) {
    var a = this.get("flightInfo");
    $jex.foreach(c, 
    function(e, d, f) {
        if (!a[f]) {
            a[f] = e;
        }
    });
};
FlightInfoManager.prototype.addFlightInfoItem = function(b, c, a) {
    this.addItem("flightInfo", b, c, a);
};
FlightInfoManager.prototype.addCorrSource = function(b, a) {
    this.addSource("corrInfo", b, a);
};
FlightInfoManager.prototype.addExtInfoSource = function(b, a) {
    this.addSource("extInfo", b, a);
};
FlightInfoManager.prototype.replacePriceData = function(c, b) {
    b = this._getPriceType(b);
    var a = this.get(b);
    $jex.foreach(c, 
    function(e, d, f) {
        a[f] = e;
    });
};
FlightInfoManager.prototype._getPriceType = function(a) {
    return (!a || a == "all") ? "my_wrappInfo": ("my_wrappInfo_" + a);
};
FlightInfoManager.prototype.updateRecommendInfo = function(c, b) {
    var a = this.get("Recommend_wrapper");
    $jex.foreach(c, 
    function(e, d, f) {
        if (!a[f]) {
            a[f] = {};
        }
        $jex.merge(a[f], c[f]);
    });
};
FlightInfoManager.prototype.addPriceDataItem = function(b, c, a) {
    this.addItem("priceData", b, c, a);
};
FlightInfoManager.prototype.addPriceGroupDataSource = function(b, a) {
    this.addSource("priceGroup", b, a);
};
FlightInfoManager.prototype.addSpecialWrapper = function(b, a) {
    this.addSource("PayCarrier", b, a);
};
FlightInfoManager.prototype.addPriceInfoSource = function(b, a) {
    this.addSource("priceInfo", b, a);
};
FlightInfoManager.prototype.addZYFAirlines = function(b, c, a) {
    this.addItem("ZYFAirlines", b, c, a);
};
FlightInfoManager.prototype.addZYFReference = function(b, c, a) {
    this.addItem("ZYFReference", b, c, a);
};
FlightInfoManager.prototype.getZYFAirlines = function(a) {
    return this.get("ZYFAirlines", a);
};
FlightInfoManager.prototype.getZYFReference = function(a) {
    return this.get("ZYFReference", a);
};
var UICacheManager = (function() {
    var a = function() {
        FlightInfoManager.superclass.constructor.call(this);
    };
    $jex.extendClass(a, InfoManager);
    a.prototype.addToCache = function(b) {
        this.addItem("uiCache", b.newid(""), b);
    };
    a.prototype.getCache = function(b) {
        return this.get("uiCache", b);
    };
    return new a();
})();
FlightInfoManager.prototype.updatePriceGroup = function(c, a) {
    var b = this.get("priceGroup", a).wrlist;
    $jex.foreach(c, 
    function(e, d, f) {
        if (!b[f]) {
            b[f] = {};
        }
        $jex.merge(b[f], c[f]);
    });
};