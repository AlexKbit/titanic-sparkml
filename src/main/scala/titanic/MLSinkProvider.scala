package titanic

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.sql.sources.StreamSinkProvider
import org.apache.spark.sql.streaming.OutputMode

abstract class MLSinkProvider extends StreamSinkProvider {
  def process(df: DataFrame): Unit

  def createSink(sqlContext: SQLContext,
                 parameters: Map[String, String],
                 partitionColumns: Seq[String],
                 outputMode: OutputMode): MLSink = MLSink(process)
}
