var DomesticOnewaySearchService = new(function() {
    var u = false;
    var p = this;
    this.param = {};
    this.oparam = {};
    var o = $jex.isdebug ? "http://local.qunar.com": "";
    var e = null;
    var s = 0;
    var x = new Date();
    var v = 0;
    var d = 0;
    var i = 0;
    var m = 0;
    var A = "";
    var h = "";
    var z = false;
    var f = false;
    var l = false;
    var w = null;
    this.longwell = function() {
        return w || {};
    };
    var c = [];
    var j = [];
    var b = null;
    var n = null;
    var t = "";
    var k = null;
    var r = false;
    this.isValidQuery = function(B) {
        if (B == null) {
            return r;
        } else {
            r = B;
        }
    };
    var y = null;
    this.queryId = function(B) {
        if (B == null) {
            return y;
        } else {
            y = B;
        }
    };
    var q = null;
    this.tserver = function(B) {
        if (B == null) {
            return q;
        } else {
            q = B;
        }
    };
    this.search = function(D) {
        if (p.searchEnd()) {
            return;
        }
        $jex.merge(this.param, {
            fromCity: D.searchDepartureAirport,
            toCity: D.searchArrivalAirport,
            fromDate: D.searchDepartureTime
        });
        $jex.merge(this.oparam, {
            ex_track: D.ex_track,
            from: D.from
        });
        var C = $jex.date.parse(this.param.fromDate);
        var B = window.SERVER_TIME || new Date();
        if (B.getTime() - C.getTime() > 86400000) {
            $jex.event.trigger(this, "expireQuery");
            return;
        }
        if (this.param.fromCity == this.param.toCity) {
            $jex.event.trigger(this, "sameCity");
            return;
        }
        this._invoke_ExtInfo();
        this._invoke_longwell();
        this.queryZYF();
        v = 1;
        e = new Date();
        setTimeout(function() {
            $jex.event.trigger(p, "pastLessSecond");
        },
        15000);
    };
    this.queryZYF = function() {
        var B = this;
        var C = o + "/zyf/api/ads.json";
        $jex.ajax(C, {
            dpt: B.param.fromCity,
            arr: B.param.toCity,
            dptDate: B.param.fromDate
        },
        function(D) {
            if (D) {
                B._process_zyf(D);
            }
        });
    };
    this.queryNext = function() {
        if (this.searchEnd()) {
            return;
        }
        $jex.console.warn("[queryNext]", new Date().getTime() - e);
        if (this.getTask()) {
            var B = this.getTask();
            $jex.console.info("queryNext: 等待插入任务结束. TaskID:", B);
            setTimeout(function() {
                p.queryNext();
            },
            100);
        } else {
            if (l == false && d != 2) {
                p._invoke_AVData();
            } else {
                if (i != 2 && (d == 2 || (d != 2 && c.length >= 2))) {
                    $jex.console.info("queryNext:处理联程", " transferSearchState:", i, " isValidQuery:", w.isValidQuery, " onewayDatasLength:", c.length);
                    p._invoke_transfer();
                } else {
                    $jex.console.info("queryNext:处理直飞");
                    setTimeout(function() {
                        p._invoke_oneway();
                    },
                    s);
                }
            }
        }
    };
    this.genTraceTimeStamp = function() {
        if (CLIENT_TIME && SERVER_TIME) {
            p.traceTimeStamp = (new Date().getTime() - CLIENT_TIME.getTime()) + SERVER_TIME.getTime();
        } else {
            p.traceTimeStamp = new Date().getTime();
        }
    };
    this.genBookingTimeStamp = function() {
        if (CLIENT_TIME && SERVER_TIME) {
            p.wrapperExpandStamp = (new Date().getTime() - CLIENT_TIME.getTime()) + SERVER_TIME.getTime();
        } else {
            p.wrapperExpandStamp = new Date().getTime();
        }
    };
    this.genFilterTimeStamp = function() {
        if (CLIENT_TIME && SERVER_TIME) {
            p.filterTimeStamp = (new Date().getTime() - CLIENT_TIME.getTime()) + SERVER_TIME.getTime();
        } else {
            p.filterTimeStamp = new Date().getTime();
        }
    };
    this._invoke_longwell = function() {
        $jex.console.start("调用longwell");
        var E = this.param;
        var C = c;
        var D = {
            "http://www.travelco.com/searchArrivalAirport": E.toCity,
            "http://www.travelco.com/searchDepartureAirport": E.fromCity,
            "http://www.travelco.com/searchDepartureTime": E.fromDate,
            "http://www.travelco.com/searchReturnTime": E.fromDate,
            locale: "zh",
            nextNDays: "0",
            searchLangs: "zh",
            searchType: "OneWayFlight",
            tags: 1
        };
        k = {
            departureCity: E.fromCity,
            arrivalCity: E.toCity,
            departureDate: E.fromDate,
            returnDate: E.fromDate,
            nextNDays: "0",
            searchType: "OneWayFlight",
            searchLangs: "zh",
            locale: "zh"
        };
        $jex.merge(D, this.oparam);
        $jex.merge(k, this.oparam);
        var B = o + "/twell/longwell";
        $jex.ajax(B, D, 
        function(H) {
            u && console.log("longwell回数", H, new Date());
            $jex.console.end("调用longwell");
            if (H.isLimit) {
                $jex.event.trigger(p, "ipBlock");
                return;
            }
            $jex.event.trigger(p, "getQueryId", H);
            w = H;
            p.queryId(H.queryID);
            k.queryID = H.queryID;
            k.serverIP = H.serverIP;
            var G = H.validate;
            if (G) {
                if (G.dept.country != "中国" || G.arri.country != "中国") {
                    $jex.event.trigger(p, "interSearch");
                    return;
                }
                if (G.dept.value == G.arri.value) {
                    $jex.event.trigger(p, "sameCity");
                    return;
                }
                $jex.event.trigger(p, "validateComplete", H.validate);
            }
            if (H.isBackendBusy) {
                $jex.event.trigger(p, "systemBusy");
                return;
            }
            if (H.isValidQuery) {
                p.isValidQuery(true);
                d = 1;
                $jex.event.trigger(p, "validQuery");
            } else {
                p.isValidQuery(false);
                d = 2;
                $jex.event.trigger(p, "invalidQuery");
            }
            if (!H.isTransferFlightsNeeded) {
                i = 2;
                $jex.event.trigger(p, "TransferDataReady");
            }
            $jex.event.trigger(p, "loadedLongwell", H);
            var F = H.oneway_data || {};
            setTimeout(function() {
                k.deduce = true;
                m = 1;
            },
            8000);
            if (!$jex.$empty(F.priceInfo)) {
                p._process_oneway(F);
            } else {
                $jex.event.trigger(p, "noOnewayData");
                p.queryNext();
            }
        },
        {
            onerror: p._onerror
        });
    };
    this._invoke_oneway = function() {
        var D = c;
        if (m == 1) {
            $jex.console.info("本次为deduce jsp调用.");
            var C = o + "/twell/flight/tags/deduceonewayflight_groupdata.jsp";
        } else {
            var C = o + "/twell/flight/tags/onewayflight_groupdata.jsp";
        }
        if (h) {
            k.flightCode = h;
        }
        var B = h;
        this._lastGinfoData = null;
        $jex.ajax(C, k, 
        function(E) {
            u && console.log("GROUP_DATA回数：", E, new Date());
            if (m == 1) {
                m = 2;
            }
            if (B !== h) {
                p.correctPriceInfo(E, h);
            }
            p._process_oneway(E);
            if (k.deduce == true) {
                f = true;
            }
        },
        {
            onerror: p._onerror
        });
    };
    this._process_oneway = function(C) {
        var B = c;
        k.status = C.status;
        B.push(C);
        if (!$jex.$empty(C.priceInfo)) {
            $jex.event.trigger(p, "loadedOnewayData", C);
            if (!z) {
                $jex.event.trigger(p, "loadedFirstData", C);
                z = true;
            }
        } else {
            $jex.console.info("直飞价格数据为空.");
        }
        if (!C.dataCompleted) {
            $jex.console.info("dataCompleted:搜索未结束");
            if (new Date() - e > 60000) {
                $jex.console.info("dataCompleted:超时停止");
                d = 2;
                p.queryNext();
            } else {
                s = $jex.$defined(C.invokeInterval) ? C.invokeInterval * 2: 100;
                x = new Date().getTime() + s;
                $jex.console.info("dataCompleted:继续搜索直飞,", s);
                p.queryNext();
            }
        } else {
            $jex.console.info("dataCompleted:搜索结束 , deduceJSPState:", m);
            if (m == 2) {
                d = 2;
            } else {
                m = 1;
            }
            this.queryNext();
        }
    };
    this._process_zyf = function(B) {
        $jex.event.trigger(p, "zyfLoaded", B);
    };
    this._invoke_transfer = function() {
        if (p.searchEnd()) {
            return;
        }
        $jex.console.info("---->调用联程");
        var C = $jex.merge({},
        k);
        if (i == 1) {
            C.isReSearch = true;
        }
        var B = o + "/twell/flight/tags/OneWayFlight_data_more.jsp";
        $jex.ajax(B, C, 
        function(D) {
            u && console.log("transfer回数：", D, new Date());
            j.push(D);
            p.tserver(D.server);
            if (D.needNewSearch == true) {
                i = 1;
                $jex.console.info("[联程需要再次调用 ] data.needNewSearch:", D.needNewSearch);
                setTimeout(function() {
                    p.queryNext();
                },
                3500);
            } else {
                $jex.event.trigger(p, "TransferDataReady");
                if (!$jex.$empty(D.data)) {
                    $jex.event.trigger(p, "loadedTransfer", D);
                    if (!z) {
                        $jex.event.trigger(p, "loadedFirstData", D);
                        z = true;
                    }
                } else {
                    $jex.event.trigger(p, "noTransferData", D);
                    $jex.console.info("联程价格数据为空.");
                }
                i = 2;
                p.queryNext();
                s = Math.max(new Date() - x, 0);
            }
        },
        {
            onerror: p._onerror
        });
    };
    this.syncCurrentFlightCode = function(B) {};
    var a = "all";
    this.invoke_flightPriceData = function(F, D, E, C) {
        a = C;
        if (D) {
            A = F;
        } else {
            h = F;
            A = "";
        }
        var B = function() {
            if (C === a) {
                E && E();
            }
        };
        p._invoke_flightPriceData(F, B);
    };
    this.correctPriceInfo = function(C, D) {
        var B = this._lastGinfoData;
        this._lastGinfoData = null;
        if (B && B.priceData[D]) {
            C.priceData = {};
            C.labelType = null;
            C.priceInfo[D] = B.priceInfo[D];
        }
    };
    this._invoke_flightPriceData = function(E, D) {
        $jex.console.info("[invoke_flightPriceData]开始调用直飞航班价格数据: flightCode:", E);
        var C = o + "/twell/flight/tags/onewayflight_groupinfo.jsp";
        var B = a;
        k.flightCode = E;
        k.label = a;
        this._lastGinfoData = null;
        $jex.ajax(C, k, 
        function(F) {
            u && console.log("groupInfo", F);
            F.flightCode = E;
            F.labelType = B;
            $jex.event.trigger(p, "loadedGroupinfo", F);
            p._lastGinfoData = F;
            $jex.event.trigger(p, "parsingFlightPriceData", F);
            if (D) {
                D();
            }
            $jex.console.info("[invoke_flightPriceData] 处理完毕");
        },
        {
            onerror: p._onerror
        });
    };
    this.searchEnd = function() {
        if (v == 2) {
            return true;
        }
        if (v != 2 && d == 2 && i == 2) {
            v = 2;
            $jex.event.trigger(p, "searchEnd");
            $jex.console.info("searchEND ::: OK ");
            return true;
        }
        return false;
    };
    this.isSearchEnd = function() {
        return v == 2;
    };
    this._invoke_ExtInfo = function() {
        $jex.console.info("调用扩展信息及准点率");
        var D = this.param;
        var B = o + "/twell/flight/DynamicFlightInfo.jsp";
        var C = {
            departureCity: D.fromCity,
            arrivalCity: D.toCity,
            departureDate: D.fromDate,
            fromCity: D.fromCity,
            toCity: D.toCity
        };
        $jex.merge(C, this.oparam);
        $jex.ajax(B, C, 
        function(E) {
            u && console.log("扩展信息回数：", E, new Date());
            b = E;
            $jex.event.trigger(p, "loadedExtInfo", E);
        },
        {
            onerror: p._onerror
        });
    };
    this._invoke_AVData = function() {
        $jex.console.info("调用AV数据");
        var B = o + "/twell/flight/OneWayFlight_Info.jsp";
        $jex.ajax(B, k, 
        function(C) {
            u && console.log("AVData回数：", C, new Date());
            n = C;
            $jex.event.trigger(p, "loadedAVData", C);
            l = true;
            if (!$jex.$empty(C.flightInfo)) {
                if (!z) {
                    $jex.event.trigger(p, "loadedFirstData", C);
                    z = true;
                }
            }
            p.queryNext();
        },
        {
            onerror: p._onerror
        });
    };
    this._onerror = function() {
        $jex.event.trigger(p, "onerror", arguments);
    };
    var g = [];
    this.insertTask = function() {
        var B = "task" + $jex.globalID();
        g.push(B);
        return B;
    };
    this.getTask = function() {
        if (g.length == 0) {
            return null;
        }
        return g[0];
    };
    this.finishTask = function(C) {
        for (var B = 0; B < g.length; B++) {
            if (g[B] == C) {
                g.splice(B, 1);
            }
        }
    };
    return this;
})();