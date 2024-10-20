package employee.processor.model

import org.apache.spark.rdd.RDD

case class AggregatedData[T1,T2](department: String,
                                 fileName: String,
                                 content: RDD[(T1,T2)])
