package titanic

import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.DataFrame

case class MLSink(process: DataFrame => Unit) extends Sink {
  override def addBatch(batchId: Long, data: DataFrame): Unit = {
    process(data)
  }
}