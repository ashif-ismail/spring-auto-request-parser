## 🚀 Spring Auto Request Parser 🚀

🌟 **Simplify your Spring Boot applications with Spring Auto Request Parser!** 🌟
This powerful library automatically configures custom argument resolution to effortlessly convert incoming HTTP requests into Java objects, based on your predefined annotations. Just annotate, and you're ready to go!

### Key Advantages 🏆

- **Zero Configuration**: Plug and play setup! No need to manually configure MVC argument resolvers.
- **Flexible**: Easily handle various content types and customize deserialization with simple annotations.
- **Lightweight**: Minimal overhead to your application while maximizing functionality.
- **Open Source**: Fully open-source allowing for community-driven improvements and transparency.

### How to Use 🛠️

1. **Add the Dependency**

   Add the following Maven dependency to your `pom.xml`:

   ```xml
   <dependency>
       <groupId>com.ashifismail</groupId>
       <artifactId>spring-auto-request-parser</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

2. **Annotate Your Controller Methods**

   Use the `@RequestContent` annotation on any controller method parameter that you want to be automatically populated from the request body:

   ```java
   import com.ashifismail.web.RequestContent;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RestController;

   @RestController
   public class MyController {

       @PostMapping("/submit")
       public String processSubmit(@RequestContent MyObject myObject) {
           return "Processed: " + myObject;
       }
   }
   ```

3. **Enjoy Automatic Request Parsing!**

   With the annotation in place, Spring Auto Request Parser takes care of the rest, ensuring that your method parameters are correctly populated based on the content type of the incoming request.

For full documentation, examples, and contributing guidelines, check out our [GitHub repository](#).

Elevate your Spring Boot applications with effortless data handling today! 🌐🚀
