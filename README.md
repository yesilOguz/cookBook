# cookBook

cookBook is an Android application aimed at sharing and viewing recipes.

## Images from app (pyhsical device)

<img src="https://github.com/yesilOguz/cookBook/blob/main/imagesForREADME/main.jpg" width="180"> | <img src="https://github.com/yesilOguz/cookBook/blob/main/imagesForREADME/recipe.jpg" width="180"> | <img src="https://github.com/yesilOguz/cookBook/blob/main/imagesForREADME/add.jpg" width="180"> 

## Installation

Download and open it in android studio.

You will need api keys afterwards.

Go to [pantry](https://getpantry.cloud) and create an account. it will give you a completely free api key. and you should paste it in the MainActivity.java file at the specified place.

```java
// You can get this key from
// https://getpantry.cloud
// signup and it will be give to you your api key
public static final String pantryId = "YOUR-API-KEY";
```

Then register at [imagebb](https://imgbb.com/signup) and just like pantry, this site will provide you with a free api key and you need to paste it into the MainActivity.java code file as follows.

```java
// You can get this key from
// https://imgbb.com/signup
// signup and it will be give to you your api key
public static final String imgbbKey = "YOUR-API-KEY";
```

After everything, you can create a basket named recipes from your pantry account, paste it into the contents of the text file named json.txt and save it and start using it.

## Usage

That's all you need to do and you're ready to build and use.

## License

[MIT](https://choosealicense.com/licenses/mit/)
