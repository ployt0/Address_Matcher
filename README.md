[![apk_build](https://github.com/ployt0/Address_Matcher/actions/workflows/generate-apk-release.yml/badge.svg)](https://github.com/ployt0/Address_Matcher/actions/workflows/generate-apk-release.yml)

This app will allow someone to find the most prominent localities, referred to as "Locality 1", 
or L1, for the first one or two letters of a postcode. This is how the post is sorted at the 
highest level. I got this information from public sources, like wikipedia, and personal knowledge.

I could add all the other localities (L2s), from the Land Registry (LR) data but:

1. It is only [licensed for personal/non-commercial use](https://www.gov.uk/government/statistical-data-sets/price-paid-data-downloads#using-or-publishing-our-price-paid-data).
2. In practice, no one cares; they just learn the localities they are paid to.

Royal Mail owns the postcode database (PAF). I can get 99.999% of it from Land Registry sales. LR
cannot grant me license to the PAF other than:

- for personal and/or non-commercial use
- to display for the purpose of providing residential property price information services

I cannot tell if both bullets above have to be met, or either.
I can't ignore that this app *is* for commercial use, by Royal Mail employees.
On the other hand, if I simply ignore postcodes, I can ignore those restrictions.

PAF is Postcode Address File. RM don't hold copyright over town names, just postcodes.
I could therefore associate L2s with counties, but counties, and even towns, can have multiple
postcodes, and even outcode areas.

Matching L1s to L2s deviates from the primary direction of Address Matcher, but it could form an
additional screen tab. We have L1 -> OC and OC -> L1, so why not L2 -> L1. If the challenge is
just poorly written Christmas handwriting, this would be beneficial. The L1s and L2s are only
approximations. L1 and L2 are probably (c) Royal Mail! L1s are approximated as "Districts",
one level more granular than counties. There are 342 recorded districts. While this may not
sound like much, I don't have a clue where half of them are! The L2s are the sum of towns
and localities. Localities are the most granular and even include business parks. There are
18000, so the risk of noise is high, but address detectives will persevere.

I do now have to add this somewhere (via some kind of floating snackbar menu prompter):

> Contains HM Land Registry data © Crown copyright and database right 2021. This data is licensed under the Open Government Licence v3.0.

Here's how I process it:

```python
import json
import os

import pandas as pd

def get_src(year):
    url = f'http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-{year}.csv'
    file_name = f'pp-{year}.csv'
    # Don't keep hitting the gov.uk servers if we can cache:
    if not os.path.isfile(f'pp-{year}.csv'):
        return url
    return file_name


names = ["tran_id", "price", "yyyymmdd_transfer", "postcode", "det_sem_ter", "newbuild", "estate_lfu", "paon",
         "saon", "street", "locality", "town", "district", "county", "cat_ab", "status_acd"]
usecols = ["locality", "town", "district" ]
src = get_src(2021)
df = pd.read_csv(src, names=names, usecols=usecols)
df = df.dropna(subset=["district"])
l2s_by_l1s = {
    k: sorted(set(g["locality"].dropna()) | set(g["town"].dropna()))
    for k, g in df.groupby("district", dropna=True)
}

# Create a long-form DataFrame combining town and locality into a single column, l2
combined = pd.concat([
    df[["district", "locality"]].rename(columns={"locality": "l2"}),
    df[["district", "town"]].rename(columns={"town": "l2"})
])

# Drop NaNs from l2 column
combined = combined.dropna(subset=["l2"])

# Group by l2 and collect unique districts
l1s_by_l2s = {
    l2: sorted(set(group["district"]))
    for l2, group in combined.groupby("l2")
}

with open("l1sToL2s.json", "w") as f:
    json.dump(l2s_by_l1s, f)

with open("l2sToL1s.json", "w") as f:
    json.dump(l1s_by_l2s, f)
```

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





