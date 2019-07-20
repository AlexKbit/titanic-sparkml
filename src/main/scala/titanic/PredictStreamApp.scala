package titanic

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object PredictStreamApp {

  def main(args: Array[String]): Unit = {

    val fileDir = "data"
    val checkpointLocation = "checkpoint"

    val spark: SparkSession = SparkSession.builder()
      .appName("Spark Prediction Streaming XGBOOST")
      .master("local[*]")
      .getOrCreate()

    val schema = TrainApp.defineSchema

      val df = spark
        .readStream
        .option("header", "true")
        .schema(schema)
        .csv(fileDir)

      df.writeStream
        .format("titanic.XGBoostMLSinkProvider")
        .queryName("XGBoostQuery")
        .option("checkpointLocation", checkpointLocation)
        .start()

    spark.streams.awaitAnyTermination()
  }
}
