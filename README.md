# Otomoto Car Finder

## Technologies used
☄ Scala 2.12.4 (Scraping, I/O)

☄ Python 3.11 (cleaning)



## Goal

I wrote this search engine in order to search for as much information as possible about the cars I was most interested in. It allows you to parametrize the make of the car, the model and the years of production of the model you are interested in.

Each one-time output is saved in the "data" folder. Each json file contains a list of advertisements. 

Each ad contains all the necessary information contained in the ads (kudos to otomoto for the detail of the ads, mobile.de falls short with them), and in addition it is possible to add the parameter "with photos" to the program, which contains embedded photos in the highest resolution contained in the ads.

## File structure issue

Initially, the lists contained objects in the format "ad id":{parameters}. When I wanted to do analysis on this data, I came to the conclusion that Spark has very big problems with interpreting the schema and reading values for single IDs. In addition, it tried to cram the entire ad information into a single table. 

Therefore, in the data folder you will find the transform.py script, which fixes the structure of each record in all the files in the folder. 

It is true that the large number of records in single files means that commas can sometimes get misplaced. That's why it's important to monitor where the "transform" script throws errors, and unfortunately, fix it manually.

## Possible improvements

- Program execution is single-threaded, a possible improvement is to use the Akka framework for multithreading, but the output structure will have to change significantly, perhaps disabling the need for data remodelling after scrapage.

- Spark job with in depth analysis to be added within the repository


