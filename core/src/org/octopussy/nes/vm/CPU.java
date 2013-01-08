package org.octopussy.nes.vm;

import org.apache.log4j.Logger;
import org.octopussy.nes.ProgramContext;

/**
 * @author octopussy
 */
public class CPU {

	private final ProgramContext mCon;

	public CPU(ProgramContext con) {
		mCon = con;
	}

	public boolean perform(int opCode) {
		int address1;
		int address2;
		byte value;
		switch (opCode){

			///////////////////////////////////////////////////////////////////////////////////////////
			// STATUS REGISTER
			case 0x78:	// SEI Set interrupt disable status 1 -> I
				mCon.setStatusRegisterBit(ProgramContext.INTERRUPT_DISABLED_FLAG, true); break;
			case 0x58:	// CLI Clear interrupt disable bit 0 -> I
				mCon.setStatusRegisterBit(ProgramContext.INTERRUPT_DISABLED_FLAG, false); break;
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
			case 0x80: // STA Absolute
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
			case 0x90: // STA Absolute + X
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
				mCon.setX(mCon.getSP()); break;
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

			default:
				Logger.getRootLogger().debug("Unknown operation code '0x" + Integer.toHexString(opCode) + "'");
				return false;
		}
		return true;
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
