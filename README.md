# amazon-produdct-api

Run as Spring Boot app

How it works -
When app is running on localhost:8080 -

http://localhost:8080/estimate?keyword=YOURPRODUCTHERE

Simply enter the above into your url bar

Example - for term 'photo frame'
http://localhost:8080/estimate?keyword=photo%20frame

Will return

```JSON
{
"keyWord": "photo frame",
"score": 48.18182
}
```

The score is a value 0-100, rating how popular of of search term your keyword is
