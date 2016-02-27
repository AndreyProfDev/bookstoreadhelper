package com.AndreyProfDev.bookstoreadhelper;

public class DataUtils
{
    public static boolean equalsNullSafe(Object first, Object second)
    {
        return first == null ? second == null : first.equals(second);
    }

    public static int calculateHashCode(Object... fieldsToCalculate)
    {
        int result = 0;

        for (Object field : fieldsToCalculate)
        {
            int fieldHash = 0;

            if (field instanceof Boolean)
            {
                fieldHash = ((Boolean) field) ? 1 : 0;
            }
            else if (field instanceof Byte || field instanceof Character
                    || field instanceof Short || field instanceof Integer)
            {
                fieldHash = (Integer) field;
            }
            else if (field instanceof Long)
            {
                fieldHash = (int)((Long) field ^ ((Long) field >>> 32));
            }
            else if (field instanceof Float)
            {
                fieldHash = Float.floatToIntBits((Float) field);
            }
            else if (field instanceof Double)
            {
                fieldHash = (int) Double.doubleToLongBits((Double) field);
            }
            else if (field != null)
            {
                fieldHash = field.hashCode();
            }

            if (field != null)
            {
                result = 31 * result + fieldHash;
            }
        }

        return result;
    }
}
