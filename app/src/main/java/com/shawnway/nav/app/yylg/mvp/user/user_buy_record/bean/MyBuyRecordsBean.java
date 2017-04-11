package com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kevin on 2017/1/6
 */

public class MyBuyRecordsBean {
    private int pageNo;
    private int pageSize;
    private String drawStatus;


    /**
     * header : {"version":"1.0","fieldErrors":[],"code":"000"}
     * body : {"page":1,"pageSize":10,"totalPage":25,"totalRecord":241,"purchaseDetailsList":[{"drawCycleId":1072,"prodName":"ddd","buyUnit":1,"cycle":18,"drawStatus":"OPEN","puredCnt":5,"totCnt":30,"leftCnt":25,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%B0%8F%E7%B1%B3%E7%BA%A2%E7%B1%B33%2F2016090320223900024_600x600.PNG","drawDetails":[{"purchSummary":"2017-01-06 15:54:34 000购买了1份","drawNumbers":"10000008 "},{"purchSummary":"2017-01-06 15:53:40 000购买了1份","drawNumbers":"10000010 "},{"purchSummary":"2017-01-06 15:48:36 000购买了1份","drawNumbers":"10000013 "},{"purchSummary":"2017-01-06 15:47:43 000购买了1份","drawNumbers":"10000002 "},{"purchSummary":"2017-01-06 14:41:39 000购买了1份","drawNumbers":"10000015 "}],"totalPurch":5},{"drawCycleId":1070,"prodName":"话费充值卡 面值20【即时开】","buyUnit":1,"cycle":21,"drawStatus":"ANNOUNCED","puredCnt":26,"totCnt":26,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E8%AF%9D%E8%B4%B9%E5%85%85%E5%80%BC%E5%8D%A1+%E9%9D%A2%E5%80%BC20%EF%BC%88%E5%8D%B3%E6%97%B6%E5%BC%80%EF%BC%89%2F2016101812373700002_600x600.png","winnerName":"155****2713","announceDate":"2017-01-06 15:46:51 000","finalRslt":"10000025","drawDetails":[{"purchSummary":"2017-01-06 15:43:50 000购买了24份","drawNumbers":"10000006 10000011 10000001 10000012 10000025 10000023 10000020 10000010 10000019 10000022 10000016 10000018 10000017 10000008 10000002 10000007 10000005 10000026 10000021 10000024 10000009 10000013 10000004 10000015 "},{"purchSummary":"2017-01-06 15:41:23 000购买了1份","drawNumbers":"10000004 "}],"totalPurch":25},{"drawCycleId":1082,"prodName":"加油卡50","buyUnit":1,"cycle":16,"drawStatus":"ANNOUNCED","puredCnt":55,"totCnt":55,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%8A%A0%E6%B2%B9%E5%8D%A1%E9%9D%A2%E5%80%BC50-1%2F2016090321034500082_600x600.png","winnerName":"155****2713","announceDate":"2017-01-06 15:54:30 000","finalRslt":"10000004","drawDetails":[{"purchSummary":"2017-01-06 15:43:35 000购买了55份","drawNumbers":"10000053 10000052 10000038 10000043 10000015 10000001 10000023 10000022 10000012 10000032 10000049 10000051 10000016 10000035 10000031 10000048 10000021 10000030 10000009 10000044 10000028 10000027 10000039 10000034 10000050 10000007 10000003 10000026 10000005 10000010 10000014 10000020 10000037 10000040 10000004 10000047 10000025 10000024 10000046 10000013 10000054 10000011 10000019 10000006 10000033 10000017 10000041 10000008 10000055 10000045 10000042 10000018 10000002 10000036 10000029 "}],"totalPurch":55},{"drawCycleId":1079,"prodName":"苏宁购物卡 面值60【开奖方式1】","buyUnit":1,"cycle":7,"drawStatus":"ANNOUNCED","puredCnt":60,"totCnt":60,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E8%8B%8F%E5%AE%81%E5%8D%A1%E9%9D%A2%E5%80%BC50-1%2F2017010310402300002_600x600.png","winnerName":"155****2713","announceDate":"2017-01-06 15:54:30 000","finalRslt":"10000039","drawDetails":[{"purchSummary":"2017-01-06 15:43:18 000购买了60份","drawNumbers":"10000057 10000021 10000024 10000043 10000027 10000001 10000051 10000052 10000035 10000012 10000040 10000020 10000056 10000059 10000023 10000017 10000049 10000044 10000007 10000033 10000045 10000004 10000060 10000018 10000005 10000039 10000002 10000009 10000015 10000022 10000008 10000013 10000010 10000011 10000031 10000036 10000058 10000047 10000032 10000042 10000016 10000041 10000006 10000046 10000003 10000050 10000026 10000025 10000054 10000055 10000029 10000028 10000034 10000019 10000048 10000038 10000014 10000053 10000037 10000030 "}],"totalPurch":60},{"drawCycleId":1077,"prodName":"小米智能灯","buyUnit":1,"cycle":10,"drawStatus":"ANNOUNCED","puredCnt":150,"totCnt":150,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%B0%8F%E7%B1%B3%E6%99%BA%E8%83%BD%E7%81%AF-%E5%8D%B3%E6%97%B6%E5%BC%80%2F2016121612015500006_600x600.PNG","winnerName":"155****2713","announceDate":"2017-01-06 15:46:01 000","finalRslt":"10000101","drawDetails":[{"purchSummary":"2017-01-06 15:42:58 000购买了150份","drawNumbers":"10000066 10000037 10000007 10000009 10000141 10000059 10000145 10000124 10000093 10000150 10000109 10000023 10000051 10000076 10000027 10000144 10000034 10000122 10000067 10000003 10000002 10000114 10000121 10000099 10000111 10000060 10000140 10000079 10000083 10000008 10000025 10000019 10000142 10000116 10000081 10000065 10000058 10000033 10000098 10000100 10000057 10000084 10000123 10000129 10000148 10000085 10000036 10000132 10000105 10000139 10000011 10000022 10000047 10000146 10000097 10000062 10000010 10000070 10000068 10000127 10000090 10000126 10000094 10000016 10000026 10000044 10000101 10000040 10000028 10000053 10000107 10000055 10000120 10000031 10000131 10000004 10000013 10000119 10000035 10000088 10000117 10000054 10000137 10000077 10000042 10000086 10000136 10000095 10000029 10000103 10000104 10000030 10000110 10000005 10000014 10000102 10000073 10000096 10000078 10000138 10000125 10000134 10000006 10000071 10000113 10000135 10000089 10000056 10000106 10000032 10000061 10000001 10000133 10000038 10000049 10000075 10000080 10000074 10000017 10000130 10000082 10000092 10000069 10000048 10000045 10000118 10000064 10000020 10000024 10000050 10000072 10000039 10000091 10000015 10000041 10000043 10000087 10000112 10000046 10000018 10000052 10000143 10000147 10000063 10000012 10000115 10000149 10000021 10000108 10000128 "}],"totalPurch":150}]}
     */

    private HeaderBean header;
    private BodyBean body;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(String drawStatus) {
        this.drawStatus = drawStatus;
    }

    public static class HeaderBean {
        /**
         * version : 1.0
         * fieldErrors : []
         * code : 000
         */

        private String version;
        private String code;
        private List<?> fieldErrors;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<?> getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(List<?> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }
    }

    public static class BodyBean {
        /**
         * page : 1
         * pageSize : 10
         * totalPage : 25
         * totalRecord : 241
         * purchaseDetailsList : [{"drawCycleId":1072,"prodName":"ddd","buyUnit":1,"cycle":18,"drawStatus":"OPEN","puredCnt":5,"totCnt":30,"leftCnt":25,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%B0%8F%E7%B1%B3%E7%BA%A2%E7%B1%B33%2F2016090320223900024_600x600.PNG","drawDetails":[{"purchSummary":"2017-01-06 15:54:34 000购买了1份","drawNumbers":"10000008 "},{"purchSummary":"2017-01-06 15:53:40 000购买了1份","drawNumbers":"10000010 "},{"purchSummary":"2017-01-06 15:48:36 000购买了1份","drawNumbers":"10000013 "},{"purchSummary":"2017-01-06 15:47:43 000购买了1份","drawNumbers":"10000002 "},{"purchSummary":"2017-01-06 14:41:39 000购买了1份","drawNumbers":"10000015 "}],"totalPurch":5},{"drawCycleId":1070,"prodName":"话费充值卡 面值20【即时开】","buyUnit":1,"cycle":21,"drawStatus":"ANNOUNCED","puredCnt":26,"totCnt":26,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E8%AF%9D%E8%B4%B9%E5%85%85%E5%80%BC%E5%8D%A1+%E9%9D%A2%E5%80%BC20%EF%BC%88%E5%8D%B3%E6%97%B6%E5%BC%80%EF%BC%89%2F2016101812373700002_600x600.png","winnerName":"155****2713","announceDate":"2017-01-06 15:46:51 000","finalRslt":"10000025","drawDetails":[{"purchSummary":"2017-01-06 15:43:50 000购买了24份","drawNumbers":"10000006 10000011 10000001 10000012 10000025 10000023 10000020 10000010 10000019 10000022 10000016 10000018 10000017 10000008 10000002 10000007 10000005 10000026 10000021 10000024 10000009 10000013 10000004 10000015 "},{"purchSummary":"2017-01-06 15:41:23 000购买了1份","drawNumbers":"10000004 "}],"totalPurch":25},{"drawCycleId":1082,"prodName":"加油卡50","buyUnit":1,"cycle":16,"drawStatus":"ANNOUNCED","puredCnt":55,"totCnt":55,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%8A%A0%E6%B2%B9%E5%8D%A1%E9%9D%A2%E5%80%BC50-1%2F2016090321034500082_600x600.png","winnerName":"155****2713","announceDate":"2017-01-06 15:54:30 000","finalRslt":"10000004","drawDetails":[{"purchSummary":"2017-01-06 15:43:35 000购买了55份","drawNumbers":"10000053 10000052 10000038 10000043 10000015 10000001 10000023 10000022 10000012 10000032 10000049 10000051 10000016 10000035 10000031 10000048 10000021 10000030 10000009 10000044 10000028 10000027 10000039 10000034 10000050 10000007 10000003 10000026 10000005 10000010 10000014 10000020 10000037 10000040 10000004 10000047 10000025 10000024 10000046 10000013 10000054 10000011 10000019 10000006 10000033 10000017 10000041 10000008 10000055 10000045 10000042 10000018 10000002 10000036 10000029 "}],"totalPurch":55},{"drawCycleId":1079,"prodName":"苏宁购物卡 面值60【开奖方式1】","buyUnit":1,"cycle":7,"drawStatus":"ANNOUNCED","puredCnt":60,"totCnt":60,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E8%8B%8F%E5%AE%81%E5%8D%A1%E9%9D%A2%E5%80%BC50-1%2F2017010310402300002_600x600.png","winnerName":"155****2713","announceDate":"2017-01-06 15:54:30 000","finalRslt":"10000039","drawDetails":[{"purchSummary":"2017-01-06 15:43:18 000购买了60份","drawNumbers":"10000057 10000021 10000024 10000043 10000027 10000001 10000051 10000052 10000035 10000012 10000040 10000020 10000056 10000059 10000023 10000017 10000049 10000044 10000007 10000033 10000045 10000004 10000060 10000018 10000005 10000039 10000002 10000009 10000015 10000022 10000008 10000013 10000010 10000011 10000031 10000036 10000058 10000047 10000032 10000042 10000016 10000041 10000006 10000046 10000003 10000050 10000026 10000025 10000054 10000055 10000029 10000028 10000034 10000019 10000048 10000038 10000014 10000053 10000037 10000030 "}],"totalPurch":60},{"drawCycleId":1077,"prodName":"小米智能灯","buyUnit":1,"cycle":10,"drawStatus":"ANNOUNCED","puredCnt":150,"totCnt":150,"leftCnt":0,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%B0%8F%E7%B1%B3%E6%99%BA%E8%83%BD%E7%81%AF-%E5%8D%B3%E6%97%B6%E5%BC%80%2F2016121612015500006_600x600.PNG","winnerName":"155****2713","announceDate":"2017-01-06 15:46:01 000","finalRslt":"10000101","drawDetails":[{"purchSummary":"2017-01-06 15:42:58 000购买了150份","drawNumbers":"10000066 10000037 10000007 10000009 10000141 10000059 10000145 10000124 10000093 10000150 10000109 10000023 10000051 10000076 10000027 10000144 10000034 10000122 10000067 10000003 10000002 10000114 10000121 10000099 10000111 10000060 10000140 10000079 10000083 10000008 10000025 10000019 10000142 10000116 10000081 10000065 10000058 10000033 10000098 10000100 10000057 10000084 10000123 10000129 10000148 10000085 10000036 10000132 10000105 10000139 10000011 10000022 10000047 10000146 10000097 10000062 10000010 10000070 10000068 10000127 10000090 10000126 10000094 10000016 10000026 10000044 10000101 10000040 10000028 10000053 10000107 10000055 10000120 10000031 10000131 10000004 10000013 10000119 10000035 10000088 10000117 10000054 10000137 10000077 10000042 10000086 10000136 10000095 10000029 10000103 10000104 10000030 10000110 10000005 10000014 10000102 10000073 10000096 10000078 10000138 10000125 10000134 10000006 10000071 10000113 10000135 10000089 10000056 10000106 10000032 10000061 10000001 10000133 10000038 10000049 10000075 10000080 10000074 10000017 10000130 10000082 10000092 10000069 10000048 10000045 10000118 10000064 10000020 10000024 10000050 10000072 10000039 10000091 10000015 10000041 10000043 10000087 10000112 10000046 10000018 10000052 10000143 10000147 10000063 10000012 10000115 10000149 10000021 10000108 10000128 "}],"totalPurch":150}]
         */

        private int page;
        private int pageSize;
        private int totalPage;
        private int totalRecord;
        private List<PurchaseDetailsListBean> purchaseDetailsList;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalRecord() {
            return totalRecord;
        }

        public void setTotalRecord(int totalRecord) {
            this.totalRecord = totalRecord;
        }

        public List<PurchaseDetailsListBean> getPurchaseDetailsList() {
            return purchaseDetailsList;
        }

        public void setPurchaseDetailsList(List<PurchaseDetailsListBean> purchaseDetailsList) {
            this.purchaseDetailsList = purchaseDetailsList;
        }

        public static class PurchaseDetailsListBean implements Serializable {
            /**
             * drawCycleId : 1072
             * prodName : ddd
             * buyUnit : 1
             * cycle : 18
             * drawStatus : OPEN
             * puredCnt : 5
             * totCnt : 30
             * leftCnt : 25
             * thumbnail : http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%B0%8F%E7%B1%B3%E7%BA%A2%E7%B1%B33%2F2016090320223900024_600x600.PNG
             * drawDetails : [{"purchSummary":"2017-01-06 15:54:34 000购买了1份","drawNumbers":"10000008 "},{"purchSummary":"2017-01-06 15:53:40 000购买了1份","drawNumbers":"10000010 "},{"purchSummary":"2017-01-06 15:48:36 000购买了1份","drawNumbers":"10000013 "},{"purchSummary":"2017-01-06 15:47:43 000购买了1份","drawNumbers":"10000002 "},{"purchSummary":"2017-01-06 14:41:39 000购买了1份","drawNumbers":"10000015 "}]
             * totalPurch : 5
             * winnerName : 155****2713
             * announceDate : 2017-01-06 15:46:51 000
             * finalRslt : 10000025
             */

            private int drawCycleId;
            private String prodName;
            private int buyUnit;
            private int cycle;
            private String drawStatus;
            private int puredCnt;
            private int totCnt;
            private int leftCnt;
            private String thumbnail;
            private int totalPurch;
            private String winnerName;
            private String announceDate;
            private String finalRslt;
            private List<DrawDetailsBean> drawDetails;

            public int getDrawCycleId() {
                return drawCycleId;
            }

            public void setDrawCycleId(int drawCycleId) {
                this.drawCycleId = drawCycleId;
            }

            public String getProdName() {
                return prodName;
            }

            public void setProdName(String prodName) {
                this.prodName = prodName;
            }

            public int getBuyUnit() {
                return buyUnit;
            }

            public void setBuyUnit(int buyUnit) {
                this.buyUnit = buyUnit;
            }

            public int getCycle() {
                return cycle;
            }

            public void setCycle(int cycle) {
                this.cycle = cycle;
            }

            public String getDrawStatus() {
                return drawStatus;
            }

            public void setDrawStatus(String drawStatus) {
                this.drawStatus = drawStatus;
            }

            public int getPuredCnt() {
                return puredCnt;
            }

            public void setPuredCnt(int puredCnt) {
                this.puredCnt = puredCnt;
            }

            public int getTotCnt() {
                return totCnt;
            }

            public void setTotCnt(int totCnt) {
                this.totCnt = totCnt;
            }

            public int getLeftCnt() {
                return leftCnt;
            }

            public void setLeftCnt(int leftCnt) {
                this.leftCnt = leftCnt;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public int getTotalPurch() {
                return totalPurch;
            }

            public void setTotalPurch(int totalPurch) {
                this.totalPurch = totalPurch;
            }

            public String getWinnerName() {
                return winnerName;
            }

            public void setWinnerName(String winnerName) {
                this.winnerName = winnerName;
            }

            public String getAnnounceDate() {
                return announceDate;
            }

            public void setAnnounceDate(String announceDate) {
                this.announceDate = announceDate;
            }

            public String getFinalRslt() {
                return finalRslt;
            }

            public void setFinalRslt(String finalRslt) {
                this.finalRslt = finalRslt;
            }

            public List<DrawDetailsBean> getDrawDetails() {
                return drawDetails;
            }

            public void setDrawDetails(List<DrawDetailsBean> drawDetails) {
                this.drawDetails = drawDetails;
            }

            public static class DrawDetailsBean implements Serializable{
                /**
                 * purchSummary : 2017-01-06 15:54:34 000购买了1份
                 * drawNumbers : 10000008
                 */

                private String purchSummary;
                private String drawNumbers;

                public String getPurchSummary() {
                    return purchSummary;
                }

                public void setPurchSummary(String purchSummary) {
                    this.purchSummary = purchSummary;
                }

                public String getDrawNumbers() {
                    return drawNumbers;
                }

                public void setDrawNumbers(String drawNumbers) {
                    this.drawNumbers = drawNumbers;
                }
            }
        }
    }
}
