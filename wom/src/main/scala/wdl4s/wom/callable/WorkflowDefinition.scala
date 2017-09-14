package wdl4s.wom.callable

import lenthall.validation.ErrorOr.ErrorOr
import wdl4s.wom.graph.{Graph, TaskCallNode}
import cats.syntax.validated._
import wdl4s.wom.expression.WomExpression
import wdl4s.wom.graph.GraphNode._

final case class WorkflowDefinition(name: String,
                                    innerGraph: Graph,
                                    meta: Map[String, String],
                                    parameterMeta: Map[String, String],
                                    declarations: List[(String, WomExpression)]) extends Callable {

  override lazy val toString = s"[Workflow $name]"
  override val graph: ErrorOr[Graph] = innerGraph.validNel

  // FIXME: how to get a meaningful order from the node set ?
  override lazy val inputs: List[_ <: Callable.InputDefinition] = innerGraph.nodes.inputDefinitions.toList

  lazy val taskCallNodes: Set[TaskCallNode] = innerGraph.nodes collect {
    case taskNode: TaskCallNode => taskNode
  }
}
