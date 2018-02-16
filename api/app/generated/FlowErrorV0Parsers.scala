/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.4.84
 * apibuilder 0.14.3 app.apibuilder.io/flow/error/0.4.84/anorm_2_6_parsers
 */
import anorm._

package io.flow.error.v0.anorm.parsers {

  import io.flow.error.v0.anorm.conversions.Standard._

  import io.flow.error.v0.anorm.conversions.Types._

  object GenericErrorCode {

    def parserWithPrefix(prefix: String, sep: String = "_"): RowParser[io.flow.error.v0.models.GenericErrorCode] = parser(prefixOpt = Some(s"$prefix$sep"))

    def parser(name: String = "generic_error_code", prefixOpt: Option[String] = None): RowParser[io.flow.error.v0.models.GenericErrorCode] = {
      SqlParser.str(prefixOpt.getOrElse("") + name) map {
        case value => io.flow.error.v0.models.GenericErrorCode(value)
      }
    }

  }

  object GenericError {

    def parserWithPrefix(prefix: String, sep: String = "_"): RowParser[io.flow.error.v0.models.GenericError] = parser(prefixOpt = Some(s"$prefix$sep"))

    def parser(
      code: String = "code",
      messages: String = "messages",
      prefixOpt: Option[String] = None
    ): RowParser[io.flow.error.v0.models.GenericError] = {
      io.flow.error.v0.anorm.parsers.GenericErrorCode.parser(prefixOpt.getOrElse("") + code) ~
      SqlParser.get[Seq[String]](prefixOpt.getOrElse("") + messages) map {
        case code ~ messages => {
          io.flow.error.v0.models.GenericError(
            code = code,
            messages = messages
          )
        }
      }
    }

  }

}