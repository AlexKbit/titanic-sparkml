package titanic

import org.apache.spark.sql.DataFrame

class XGBoostMLSinkProvider extends MLSinkProvider {
  override def process(df: DataFrame) {
    XGBoostModel.transform(df)
  }
}