Royal Mail owns the postcode database. I can get 99.99% of it from Land Registry sales. The postcodes are, apparently and as I recall, still copy righted.

This app will allow someone to find the most prominent localities, referred to as "Locality 1", or L1, for the first one or two letters of a postcode. This is how the post is sorted at the highest level. I could add all the other localities (L2s), from the Land Registry (LR) data but for:

1. I won't profit to.
2. I may be breaching copyright.
3. The app could become unwieldy.
4. In practice, no one cares; they just learn the localities they are paid to.

## Why

- It might help someone.
- It was almost 10 years since I last programmed Android. I have a lot of legacy code to update. A lot has changed with Android Studio, Kotlin, and Jetpack Compose.
- It's *throwaway* code I can make public and see how Github helps me to test an Android app.
- This is how "outward" (mail going to other regions) mail is sorted
  - Sorters, often with no prior knowledge, need to recognise the outcode area


## Demo

0.0.1:

https://github.com/user-attachments/assets/63abd394-d5d5-470f-b7ce-6ef991693175

It is available with source code under "releases". Alternatively, you can find non-versioned
APKs under each run of the CI pipeline, in the Actions tab.