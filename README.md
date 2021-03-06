# Amazon-product-api

This is an app used to return search term popularity(score) of a product(defined by the user) on Amazon. 

This is a RESTful Web Service App Using Java 9 and Spring Boot. 

This is done by using Amazon's open product serch API, which will return a list of products related to the search term used. You can see this in action on Amazons main page.. if you type in a letter, you get a auto suggestion of 10 searchterms/products that are most asscoiated with that character. 

i.e. typing 'i' you will see a product list with iphone, ipod, ipad.. as these are very popular products, and so they will have a high score according to this app.
Where as you might have to type more than 5-6 characters before another product is suggested, in which it will have a lower score.

So with this app, a very popular product like an Iphone will have a high score, where as an obscure one like a door handle might have a low one. All based on the public Amazon auto search term api. 


How it works 
------
 * **Get product score for your desired product**

```
GET - http://localhost:8080/estimate/{product} i.e.http://localhost:8080/estimate/photo frame
```
Will return

```JSON
{
"product": "photo frame",
"score": 48.18182
}
```

The score is a value 0-100, rating how popular your keyword is as a search term on Amazon

### Spec -
------
* Accepts JSON 
* Response in JSON 
* JDK9 or higher
* Build with Maven
* Lombok has been used to reduce boilerplate code
* Google's Gson library has been used to process the JSON from the API

### Running
Run as a Spring Boot App
