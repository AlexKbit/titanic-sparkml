package titanic

import ml.dmlc.xgboost4j.scala.spark.XGBoostEstimator
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}

object TrainApp {

  def main(args: Array[String]): Unit = {

    val spark  = SparkSession.builder()
      .appName("Spark XGBOOST Titanic Training")
      .master("local[*]")
      .getOrCreate()

    val filePath = "data/train.csv"
    val modelPath = "model"

    val schema = defineSchema

    val df_raw = spark
      .read
      .option("header", "true")
      .schema(schema)
      .csv(filePath)

    val df = df_raw.na.fill(0)

    val sexIndexer = new StringIndexer()
      .setInputCol("Sex")
      .setOutputCol("SexIndex")
      .setHandleInvalid("keep")

    val cabinIndexer = new StringIndexer()
      .setInputCol("Cabin")
      .setOutputCol("CabinIndex")
      .setHandleInvalid("keep")

    val embarkedIndexer = new StringIndexer()
      .setInputCol("Embarked")
      .setOutputCol("EmbarkedIndex")
      .setHandleInvalid("keep")

    val vectorAssembler = new VectorAssembler()
      .setInputCols(Array("Pclass", "SexIndex", "Age", "SibSp", "Parch", "Fare", "CabinIndex", "EmbarkedIndex"))
      .setOutputCol("features")

    val xgbEstimator = new XGBoostEstimator(Map[String, Any]("num_rounds" -> 100))
      .setFeaturesCol("features")
      .setLabelCol("Survival")

    val pipeline = new Pipeline().setStages(Array(sexIndexer, cabinIndexer, embarkedIndexer, vectorAssembler, xgbEstimator))
    val cvModel = pipeline.fit(df)
    cvModel.write.overwrite.save(modelPath)
  }

  def defineSchema: StructType = {
    StructType(
      Array(StructField("PassengerId", IntegerType),
        StructField("Survival", IntegerType),
        StructField("Pclass", DoubleType),
        StructField("Name", StringType),
        StructField("Sex", StringType),
        StructField("Age", DoubleType),
        StructField("SibSp", DoubleType),
        StructField("Parch", DoubleType),
        StructField("Ticket", StringType),
        StructField("Fare", DoubleType),
        StructField("Cabin", StringType),
        StructField("Embarked", StringType)
      ))
  }

}
