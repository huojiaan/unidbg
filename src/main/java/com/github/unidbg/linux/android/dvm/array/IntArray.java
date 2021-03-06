package com.github.unidbg.linux.android.dvm.array;

import com.github.unidbg.Emulator;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.pointer.UnicornPointer;
import com.sun.jna.Pointer;

public class IntArray extends BaseArray<int[]> implements PrimitiveArray<int[]> {

    public IntArray(int[] value) {
        super(value);
    }

    @Override
    public int length() {
        return value.length;
    }

    public void setValue(int[] value) {
        super.value = value;
    }

    @Override
    public void setData(int start, int[] data) {
        System.arraycopy(data, 0, value, start, data.length);
    }

    @Override
    public UnicornPointer _GetArrayCritical(Emulator<?> emulator, Pointer isCopy) {
        if (isCopy != null) {
            isCopy.setInt(0, VM.JNI_TRUE);
        }
        UnicornPointer pointer = this.allocateMemoryBlock(emulator, value.length * 4);
        pointer.write(0, value, 0, value.length);
        return pointer;
    }

    @Override
    public void _ReleaseArrayCritical(Pointer elems, int mode) {
        switch (mode) {
            case VM.JNI_COMMIT:
                this.setValue(elems.getIntArray(0, this.value.length));
                break;
            case 0:
                this.setValue(elems.getIntArray(0, this.value.length));
            case VM.JNI_ABORT:
                this.freeMemoryBlock(elems);
                break;
        }
    }
}
