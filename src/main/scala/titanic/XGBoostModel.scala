package titanic

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.{DataFrame, SaveMode}

object XGBoostModel {

  private val modelPath = "model"

  private val model = PipelineModel.read.load(modelPath)

  def transform(df: DataFrame) = {
    val df_clean = df.na.fill(0)
    val result = model.transform(df_clean)

    result
      .select("PassengerId", "prediction")
      .coalesce(1)
      .write
      .mode(SaveMode.Append)
      .csv("result")
  }

}
