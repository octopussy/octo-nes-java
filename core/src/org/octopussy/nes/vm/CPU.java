package org.octopussy.nes.vm;

import org.octopussy.nes.Logger;
import org.octopussy.nes.OctoMath;
import org.octopussy.nes.ProgramContext;
import org.octopussy.nes.mappers.MemoryMapper;

/**
 * @author octopussy
 */
public class CPU {
	private static final int NMI_ADDRESS = 0xfffa;
	private static final int RESET_ADDRESS = 0xfffc;
	private static final int IRQ_BRK_ADDRESS = 0xfffe;

	private final ProgramContext mCon;
	private final MemoryMapper mMemoryMapper;


	public CPU(MemoryMapper memoryMapper) {
		mMemoryMapper = memoryMapper;
		mCon = new ProgramContext(memoryMapper);
	}

	public boolean performNextInstruction() {
		int opCode = mCon.consumeOpCode();
		int address1;
		int address2;
		byte value;
		switch (opCode){

			case 0x00: // BRK
				performBRK();
				break;
			///////////////////////////////////////////////////////////////////////////////////////////
			// STATUS REGISTER
			case 0x78:	// SEI Set interrupt disable status 1 -> I
				mCon.setStatusRegisterBit(ProgramContext.IRQ_DISABLED_FLAG, true); break;
			case 0x58:	// CLI Clear interrupt disable bit 0 -> I
				mCon.setStatusRegisterBit(ProgramContext.IRQ_DISABLED_FLAG, false); break;
			case 0x38:	// SEC   Set Carry Flag 1 -> C
				mCon.setStatusRegisterBit(ProgramContext.CARRY_FLAG, true); break;
			case 0x18:	// CLC Clear Carry Flag 0 -> C
				mCon.setStatusRegisterBit(ProgramContext.CARRY_FLAG, false); break;
			case 0xF8 :	// SED Set Decimal Mode 1 -> D
				mCon.setStatusRegisterBit(ProgramContext.DECIMAL_MODE, true); break;
			case 0xD8:	// CLD Clear Decimal Mode 0 -> D
				mCon.setStatusRegisterBit(ProgramContext.DECIMAL_MODE, false); break;
			case 0xB8:	//	CLV Clear Overflow Flag 0 -> V
				mCon.setStatusRegisterBit(ProgramContext.OVERFLOW_FLAG, false); break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// LDk Load register with value/memory
			case 0xA9: // LDA Immediate
				mCon.setAcc(mCon.consumeByte()); break;
			case 0xA2: // LDX Immediate
				mCon.setX(mCon.consumeByte()); break;
			case 0xA0: // LDY Immediate
				mCon.setY(mCon.consumeByte()); break;
			case 0xA5: // LDA Zero page
				mCon.setAcc(getByteInMemByZeroPageOperand()); break;
			case 0xA6: // LDX Zero page
				mCon.setX(getByteInMemByZeroPageOperand()); break;
			case 0xA4: // LDY Zero page
				mCon.setY(getByteInMemByZeroPageOperand()); break;
			case 0xAD: // LDA Absolute
				mCon.setAcc(getByteInMemByAbsOperand()); break;
			case 0xAE: // LDX Absolute
				mCon.setX(getByteInMemByAbsOperand()); break;
			case 0xAC: // LDY Absolute
				mCon.setY(getByteInMemByAbsOperand()); break;
			case 0xB5: // LDA Zero page + X
				mCon.setAcc(getByteInMemByZeroPageIndexedOperand(mCon.getX())); break;
			case 0xB6: // LDX Zero page + Y
				mCon.setX(getByteInMemByZeroPageIndexedOperand(mCon.getY())); break;
			case 0xB4: // LDY Zero page + X
				mCon.setY(getByteInMemByZeroPageIndexedOperand(mCon.getX())); break;
			case 0xBD: // LDA Absolute + X
				mCon.setAcc(getByteInMemByAbsIndexedOperand(mCon.getX())); break;
			case 0xB9: // LDA Absolute + Y
				mCon.setAcc(getByteInMemByAbsIndexedOperand(mCon.getY())); break;
			case 0xBE: // LDX Absolute + Y
				mCon.setX(getByteInMemByAbsIndexedOperand(mCon.getY())); break;
			case 0xBC: // LDY Absolute + X
				mCon.setY(getByteInMemByAbsIndexedOperand(mCon.getX())); break;
			case 0xA1: // LDA (Indirect, X)
				//throw new NotImplementedException();

				address1 = (mCon.consumeZeroPageAddress() + (mCon.getX() & 0xff)) & 0xff;
				address2 = mCon.getWordInMemory(address1) & 0xffff;
				value = mCon.getByteInMemory(address2);
				mCon.setAcc(value);
				break;
			case 0xB1: // LDA (Indirect), Y
				//throw new NotImplementedException();

				address1 = mCon.consumeZeroPageAddress();
				address2 = mCon.readAbsAddressInMem(address1) + (mCon.getY() & 0xff);
				value = mCon.getByteInMemory(address2);
				mCon.setAcc(value);
				break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// STk Store register in memory
			case 0x85: // STA Zero page
				setByteInMemByZeroPageOperand(mCon.getAcc()); break;
			case 0x86: // STX Zero page
				setByteInMemByZeroPageOperand(mCon.getX()); break;
			case 0x84: // STY Zero page
				setByteInMemByZeroPageOperand(mCon.getY()); break;
			case 0x8D: // STA Absolute
				setByteInMemByAbsOperand(mCon.getAcc()); break;
			case 0x8E: // STX Absolute
				setByteInMemByAbsOperand(mCon.getX()); break;
			case 0x8C: // STY Absolute
				setByteInMemByAbsOperand(mCon.getY()); break;
			case 0x95: // STA Zero page + X
				setByteInMemByZeroPageIndexedOperand(mCon.getX(), mCon.getAcc()); break;
			case 0x96: // STX Zero page + Y
				setByteInMemByZeroPageIndexedOperand(mCon.getY(), mCon.getX()); break;
			case 0x94: // STY Zero page + X
				setByteInMemByZeroPageIndexedOperand(mCon.getX(), mCon.getY()); break;
			case 0x9D: // STA Absolute + X
				setByteInMemByAbsIndexedOperand(mCon.getX(), mCon.getAcc()); break;
			case 0x99: // STA Absolute + Y
				setByteInMemByAbsIndexedOperand(mCon.getY(), mCon.getAcc()); break;
			case 0x81: // STA (Indirect, X)
				// TODO: implement
				break;
			case 0x91: // STA (Indirect), Y
				// TODO: implement
				break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// Transfers between registers/pointers
			case 0xAA: // TAX Transfer accumulator to index X
				mCon.setX(mCon.getAcc()); break;
			case 0xA8: // TAY Transfer accumulator to index Y
				mCon.setY(mCon.getAcc()); break;
			case 0xBA: // TSX Transfer stack pointer to index X
				mCon.setX((byte)(mCon.getSP() & 0xff)); break;
			case 0x8A: // TXA Transfer index X to accumulator
				mCon.setAcc(mCon.getX()); break;
			case 0x9A: // TXS Transfer index X to stack pointer
				mCon.setSP(mCon.getX()); break;
			case 0x98: // TYA Transfer index Y to accumulator
				mCon.setAcc(mCon.getY()); break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// Increment memory by one
			case 0xE6: // INC Zero page
				mCon.incMemoryByte(mCon.consumeZeroPageAddress()); break;
			case 0xF6: // INC Zero page + X
				mCon.incMemoryByte(mCon.consumeZeroPageAddress() + (mCon.getX() & 0xff)); break;
			case 0xEE: // INC Absolute
				mCon.incMemoryByte(mCon.consumeAbsAddress()); break;
			case 0xFE: // INC Absolute + X
				mCon.incMemoryByte(mCon.consumeAbsAddress() + (mCon.getX() & 0xff)); break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// Decrement memory by one
			case 0xC6: // DEC Zero page
				mCon.decMemoryByte(mCon.consumeZeroPageAddress()); break;
			case 0xD6: // DEC Zero page + X
				mCon.decMemoryByte(mCon.consumeZeroPageAddress() + (mCon.getX() & 0xff)); break;
			case 0xCE: // DEC Absolute
				mCon.decMemoryByte(mCon.consumeAbsAddress()); break;
			case 0xDE: // DEC Absolute + X
				mCon.decMemoryByte(mCon.consumeAbsAddress() + (mCon.getX() & 0xff)); break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// Increment / decrement registers
			case 0xE8: // INX Increment Index X by one
				mCon.setX(OctoMath.incByte(mCon.getX())); break;
			case 0xC8: // INY Increment Index Y by one
				mCon.setY(OctoMath.incByte(mCon.getY())); break;
			case 0xCA: // DEX Decrement Index X by one
				mCon.setX(OctoMath.decByte(mCon.getX())); break;
			case 0x88: // DEY Decrement Index Y by one
				mCon.setY(OctoMath.decByte(mCon.getY())); break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// BIT Test bits in memory with accumulator
			case 0x24: // Zero page
			case 0x2C: // Absolute
				value = opCode == 0x24 ? getByteInMemByZeroPageOperand() : getByteInMemByAbsOperand();
				mCon.setStatusRegisterBit(ProgramContext.OVERFLOW_FLAG, (0x40 & value) != 0);
				mCon.setSignFlag(value);
				mCon.setZeroFlag((byte)(value & mCon.getAcc()));
				break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// Branching
			case 0x10: // BPL result >= 0
				address1 = mCon.consumeRelativeAddress();
				if (!mCon.getStatusBit(ProgramContext.SIGN_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0x30: // BMI result < 0
				address1 = mCon.consumeRelativeAddress();
				if (mCon.getStatusBit(ProgramContext.SIGN_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0x50: // BVC no overflow
				address1 = mCon.consumeRelativeAddress();
				if (!mCon.getStatusBit(ProgramContext.OVERFLOW_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0x70: // BVC overflow
				address1 = mCon.consumeRelativeAddress();
				if (mCon.getStatusBit(ProgramContext.OVERFLOW_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0x90: // BCC no carry
				address1 = mCon.consumeRelativeAddress();
				if (!mCon.getStatusBit(ProgramContext.CARRY_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0xB0: // BCC carry
				address1 = mCon.consumeRelativeAddress();
				if (mCon.getStatusBit(ProgramContext.CARRY_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0xD0: // BNE not zero
				address1 = mCon.consumeRelativeAddress();
				if (!mCon.getStatusBit(ProgramContext.ZERO_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0xF0: // BEQ zero
				address1 = mCon.consumeRelativeAddress();
				if (mCon.getStatusBit(ProgramContext.ZERO_FLAG))
					mCon.jumpRelative(address1);
				break;
			case 0x4C: // JMP Absolute
				mCon.jumpAbsolute(mCon.consumeAbsAddress());
				break;
			case 0x6C: // JMP Indirect
				address1 = mCon.consumeAbsAddress();
				address2 = mCon.getWordInMemory(address1) & 0xffff;
				mCon.jumpAbsolute(address2);
				break;
			case 0x20: // JSR Jump to new location saving return address
				mCon.pushAddress(mCon.getProgramCounter() + 2);
				mCon.jumpAbsolute(mCon.consumeAbsAddress());
				break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// CMP Compare memory and accumulator
			case 0xC9: // Immediate
				compareBytes(mCon.consumeByte(), mCon.getAcc()); break;

			case 0xDD: // Absolute + X
				compareBytes(getByteInMemByAbsIndexedOperand(mCon.getX()), mCon.getAcc()); break;

			///////////////////////////////////////////////////////////////////////////////////////////
			// ORA "OR" memory with accumulator
			case 0x09: // Immediate
				mCon.setAcc(logicOr(mCon.consumeByte(), mCon.getAcc())); break;
//			case 0x05: // ZP
//				break;
//			case 0x15: // ZP + X
//				break;
//			case 0x0D: // Abs
//				break;
//			case 0x1D: // Abs + X
//				break;
//			case 0x19: // Abs + Y
//				break;
//			case 0x01: // Indirect (,X)
//				break;
//			case 0x11: // Indirect (,) Y
//				break;

			// Future expansions
			case 0x02: case 0x03: case 0x04: case 0x07: case 0x0B: case 0x0C: case 0x0F:
			case 0x12: case 0x13: case 0x14: case 0x1A: case 0x1B: case 0x1C: case 0x1F:
			case 0x43: case 0x4F:
			case 0x80:
			case 0xFA: case 0xFB:
				Logger.debug("Future expansion opcode '0x" + Integer.toHexString(opCode) + "'");
				return true;
			default:
				Logger.debug("Unknown operation code '0x" + Integer.toHexString(opCode) + "'");
				return false;
		}
		return true;
	}

	public void performReset() {
		performInterrupt(mCon.getWordInMemory(RESET_ADDRESS) & 0xffff);
	}
	
	public void performNMI(){
		performInterrupt(mCon.getWordInMemory(NMI_ADDRESS) & 0xffff);
	}

	public void performBRK() {
		if (!mCon.getStatusBit(ProgramContext.IRQ_DISABLED_FLAG))
			performInterrupt(mCon.getWordInMemory(IRQ_BRK_ADDRESS) & 0xffff);
	}

	private void performInterrupt(int interruptAddress){
		int returnAddress = mCon.getProgramCounter() + 1;
		mCon.pushAddress(returnAddress);
		mCon.setStatusRegisterBit(ProgramContext.BREAK, true);
		mCon.push(mCon.getStatusRegister());
		mCon.setStatusRegisterBit(ProgramContext.IRQ_DISABLED_FLAG, true);
		mCon.jumpAbsolute(interruptAddress);
	}

	private byte logicOr(byte src, byte r) {
		byte res = (byte)(src | r);
		mCon.setSignFlag(res);
		mCon.setZeroFlag(res);
		return res;
	}

	private void compareBytes(byte src, byte r) {
		int cmp = (r & 0xff) - (src & 0xff);
		mCon.setStatusRegisterBit(ProgramContext.CARRY_FLAG, cmp < 0x100);
		mCon.setSignFlag((byte) (cmp & 0xff));
		mCon.setZeroFlag((byte)(cmp & 0xff));
	}

	private byte getByteInMemByAbsIndexedOperand(byte indexOperandValue) {
		int address = mCon.consumeAbsAddress() + (indexOperandValue & 0xff);
		return mCon.getByteInMemory(address);
	}

	private void setByteInMemByAbsIndexedOperand(byte indexOperandValue, byte value) {
		int address = mCon.consumeAbsAddress() + (indexOperandValue & 0xff);
		mCon.setByteInMemory(address, value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	private byte getByteInMemByZeroPageIndexedOperand(byte indexOperandValue) {
		int address = mCon.consumeZeroPageAddress() + (indexOperandValue & 0xff);
		return mCon.getByteInMemory(address);
	}

	private void setByteInMemByZeroPageIndexedOperand(byte indexOperandValue, byte value) {
		int address = mCon.consumeZeroPageAddress() + (indexOperandValue & 0xff);
		mCon.setByteInMemory(address, value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	private byte getByteInMemByAbsOperand() {
		int address = mCon.consumeAbsAddress();
		return mCon.getByteInMemory(address);
	}

	private void setByteInMemByAbsOperand(byte value) {
		mCon.setByteInMemory(mCon.consumeAbsAddress(), value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	private byte getByteInMemByZeroPageOperand() {
		int address = mCon.consumeZeroPageAddress();
		return mCon.getByteInMemory(address);
	}

	private void setByteInMemByZeroPageOperand(byte value) {
		mCon.setByteInMemory(mCon.consumeZeroPageAddress(), value);
	}
}
