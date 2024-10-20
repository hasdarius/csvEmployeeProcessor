This is a Scala Spark job written to process files in a folder.

Scala version used: 2.12.18

Spark version used: 3.5.3

Example of usage:
```
spark-submit --class employee.processor.Main --master local[4] --conf spark.driver.extraJavaOptions=-Dconfig.file=pathTo/application.conf --conf spark.executor.extraJavaOptions=-Dconfig.file=pathTo/application.conf --files pathTo/application.conf ./target/csv-employee-processor-0.1.0-jar-with-dependencies.jar 
```

The job, after it has been packed into a jar using ```mvn clean install```, runs only if it has been served an
external configuration file with the extension ```.conf```. Beware to run the jar with suffix ```-jar-with-dependecies.jar``` as it represents a fat jar with the library dependencies needed.

## Mandatory Application Properties

As mentioned above and as it can be seen in the ```spark-submit ``` command, when submitting the job we need to pass ```pathTo/application.conf``` full path to an external application properties file.

This property file works with the Typesafe Config library in order to extract properties from an external config file that is injected into the Spark context at job submit.

The following configuration settings are mandatory and must be provided in the `application.conf` file:

| Configuration Key               | Description                                      |
|----------------------------------|--------------------------------------------------|
| `app.input-folder-location`      | Path to the folder from which data is read.      |
| `app.output-folder-location`     | Path to the folder to which data is written.     |

### Usage

These values must be specified in the `application.conf` file, and they are required for the application to run properly. Ensure that both the input and output folder paths are correctly set, 
otherwise the spark job will fail.

## Optional Application Properties

The application also uses the following configuration settings, which can be provided via the `application.conf` file. If a variable is not set, the default value will be used.

| Configuration Key             | Default Value | Description                                    |
|-------------------------------|---|------------------------------------------------|
| `app.spark_master`            | `local[*]` | Defines the Spark master URL.                  |
| `app.spark_name`              | `CsvEmployeeProcessor` | Defines the Spark application name.            |
| `app.file_type`               | `.csv`                 | Defines the file type (e.g., `.csv`).          |
| `app.csv.delimiter`           | `,`                    | Defines the CSV delimiter.                     |
| `app.date_format`             | `yyyy-MM-dd`           | Defines the date format pattern.               |

### Usage

You can override these values by modifying the `application.conf` file.

### Application Properties file example
```properties
app {
  input-folder-location = ${?INPUT_FOLDER_LOCATION} //reads environment variable into property
  output-folder-location = ${?OUTPUT_FOLDER_LOCATION} ////reads environment variable into property
}

```


### Implementation notes

* There are different metrics that are calculated both at company and at department level.
The output is written either using Spark to folders or by using Scala/Java I/O API to .csv files. 
The second option can be a viable option for this specific use case as the aggregation results are not represent by a big amount of data.


* Nevertheless, the preferred option is to write the results using Spark, but with one partition per aggregation result, i.e. one file.  


* Initially, the solution was written using Scala 2.13, but due to Spark incompatibility, I reverted back to Scala 2.12. 
The main difference was that instead of LazyList I use now Stream, which is deprecated in 2.13 