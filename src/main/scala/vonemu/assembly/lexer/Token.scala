package vonemu.assembly.lexer

import scala.util.parsing.input.Positional


//.runtime.{universe => ru}
/*
 * Missing:
 * WORD PTR | BYTE PTR
 * dup in variable definition
 * 
 * 
 * Issues:
 * 1000B gets parsed correctly as literalInteger(1000)
 */

object Token{
  
  def special = List(COMMA(),NEWLINE(),EMPTY())
  def keyword= List( END(),NOP(),RET(),HLT())
  def ops = List(ORG(),MOV(),CMP())++binaryArithmetic++unaryArithmetic
  def binaryArithmetic = List(ADD(),ADC(),SUB(),SBB(),OR(),XOR(),AND())
  def unaryArithmetic = List(INC(),DEC(),NOT())
  def registers:List[RegisterToken] = lRegisters++hRegisters++xRegisters++List(SP())
  def lRegisters =List(AL(),BL(),CL(),DL())
  def hRegisters =List(AH(),BH(),CH(),DH())
  def xRegisters =List(AX(),BX(),CX(),DX())
  def inputOutput= List(IN(),OUT())
  def jump= List(JMP(),CALL())++conditionalJump
  def conditionalJump=List(JC(),JNC(),JZ(),JNZ(),JO(),JNO(),JS(),JNS())
  def interrupt = List(CLI(),STI(),IRET(),INT())
  def stack = List(PUSH(),POP())
  def flagsStack = List(PUSHF(),POPF())
  def varType = List(DB(),DW())
  
//  def brackets = List(OPENBRACKET(),CLOSEBRACKET())
 
}



sealed trait Token extends Positional with Product with Serializable

case class UNKNOWN() extends Token

sealed trait Value extends Token
sealed trait Mutable extends Value

sealed trait IORegister

sealed trait IOAddress
case class LABEL(str: String) extends Special
case class INDIRECTBX() extends Mutable
case class IDENTIFIER(str: String) extends Mutable with IOAddress

sealed trait Literal extends Token
case class LITERALSTRING(str: String) extends Literal with Value
case class LITERALINTEGER(v: Int) extends Literal with Value with IOAddress

sealed trait Interrupt extends InstructionToken
case class CLI() extends Interrupt
case class STI() extends Interrupt
case class IRET() extends Interrupt
case class INT() extends Interrupt

sealed trait StackInstruction extends InstructionToken
case class PUSH() extends StackInstruction
case class POP() extends StackInstruction
sealed trait StackFlagsInstruction extends InstructionToken
case class PUSHF() extends StackFlagsInstruction
case class POPF() extends StackFlagsInstruction

sealed trait VarType extends Token
case class DW() extends VarType 
case class DB() extends VarType

sealed trait Special extends Token
case class COMMA() extends Special 
case class NEWLINE() extends Special 
case class EMPTY() extends Special
case class UNINITIALIZED() extends Special
//case class OPENBRACKET() extends Special
//case class CLOSEBRACKET() extends Special

trait InstructionToken extends Token
case class RET() extends InstructionToken
case class MOV() extends InstructionToken

case class NOP() extends InstructionToken
case class END() extends InstructionToken
case class ORG() extends InstructionToken
case class HLT() extends InstructionToken

sealed trait IOToken extends InstructionToken
case class IN() extends IOToken
case class OUT() extends IOToken

trait ArithmeticOp extends InstructionToken

trait BinaryArithmeticOp extends ArithmeticOp
case class ADD() extends BinaryArithmeticOp
case class ADC() extends BinaryArithmeticOp
case class SUB() extends BinaryArithmeticOp
case class SBB() extends BinaryArithmeticOp
case class NOR() extends BinaryArithmeticOp
case class AND() extends BinaryArithmeticOp
case class OR() extends BinaryArithmeticOp
case class XOR() extends BinaryArithmeticOp
case class CMP() extends BinaryArithmeticOp
trait UnaryArithmeticOp extends ArithmeticOp
case class DEC() extends UnaryArithmeticOp
case class INC() extends UnaryArithmeticOp
case class NOT() extends UnaryArithmeticOp

trait JumpInstructionToken extends InstructionToken
case class JMP() extends JumpInstructionToken
case class CALL() extends JumpInstructionToken

trait ConditionalJumpToken extends JumpInstructionToken 
case class JC() extends ConditionalJumpToken
case class JNC() extends ConditionalJumpToken
case class JS() extends ConditionalJumpToken
case class JNS() extends ConditionalJumpToken
case class JO() extends ConditionalJumpToken
case class JNO() extends ConditionalJumpToken
case class JZ() extends ConditionalJumpToken
case class JNZ() extends ConditionalJumpToken


trait RegisterToken extends Token with Mutable with Value
case class SP() extends RegisterToken
trait FullRegisterToken extends RegisterToken
case class AX() extends FullRegisterToken with IORegister
case class BX() extends FullRegisterToken
case class CX() extends FullRegisterToken
case class DX() extends FullRegisterToken with IOAddress
trait LowRegisterToken extends RegisterToken
case class AL() extends LowRegisterToken with IORegister
case class BL() extends LowRegisterToken
case class CL() extends LowRegisterToken
case class DL() extends LowRegisterToken
trait HighRegisterToken extends RegisterToken
case class AH() extends HighRegisterToken
case class BH() extends HighRegisterToken
case class CH() extends HighRegisterToken
case class DH() extends HighRegisterToken




