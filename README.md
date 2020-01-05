# amazon-produdct-api

Run as Spring Boot app

How it works -
When app is running on localhost:8080 -
http://localhost:8080/estimate?keyword=YOURPRODUCTHERE
Simply enter the above into your url bar

Example - for term 'photo frame'
http://localhost:8080/estimate?keyword=photo%20frame

Will return

{
"keyWord": "photo frame",
"score": 48.18182
}
