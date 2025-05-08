const APIKEY = 'D5EFCFED-9FE2-3815-868B-DB16D0622312';
var cityData = {
    service:'data',
    version:2.0,
    request:'GetFeature',
    format:'json',
    errorformat:'json',
    size:10,
    page:1,
    data:'LT_C_ADSIDO_INFO',
    geomfilter:'POINT(126.9395 37.51245)',
    attrfilter:'ctprvn_cd:like:11',
    columns:'ctprvn_cd,ctp_kor_nm,ctp_eng_nm,ag_geom',
    geometry:true,
    attribute:true,
    crs:'EPSG:4326',
    domain:'http://127.0.0.1:80',
    key:'D5EFCFED-9FE2-3815-868B-DB16D0622312'
}


function getCityBoundaryData() {
    $.ajax({
        type : 'GET',
        url : 'https://api.vworld.kr/req/data',
        dataType : 'json',
        data : cityData
    }).done(function(data){
        console.log(data);
    }).fail(function(error){
        alert(JSON.stringify(error));
    })
}
