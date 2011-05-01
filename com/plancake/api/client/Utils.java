/*************************************************************************************
* ===================================================================================*
* Software by: Danyuki Software Limited                                              *
* This file is part of Plancake.                                                     *
*                                                                                    *
* Copyright 2009-2010-2011 by:     Danyuki Software Limited                          *
* Support, News, Updates at:  http://www.plancake.com                                *
* Licensed under the AGPL version 3 license.                                         *                                                       *
* Danyuki Software Limited is registered in England and Wales (Company No. 07554549) *
**************************************************************************************
* Plancake is distributed in the hope that it will be useful,                        *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                     *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      *
* GNU Affero General Public License for more details.                                *
*                                                                                    *
* You should have received a copy of the GNU Affero General Public License           *
* along with this program.  If not, see <http://www.gnu.org/licenses/>.              *
*                                                                                    *
**************************************************************************************/

package com.plancake.api.client;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils
{
   /**
    * Computes the md5 sum a la PHP md5(string)
    * 
    * @param input
    * @return
    * @throws NoSuchAlgorithmException
    */
   public static String md5(String input) throws NoSuchAlgorithmException {
       String result = input;
       if(input != null) {
           MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
           md.update(input.getBytes());
           BigInteger hash = new BigInteger(1, md.digest());
           result = hash.toString(16);
           if ((result.length() % 2) != 0) {
               result = "0" + result;
           }
       }
       return result;
   }
   
   /**
    * Returns true if the input is '1', false otherwise
    * 
    * @param input
    * @return
    */
   public static boolean convertFromStringToBoolean(String input)
   {
	   return input.equals("1");
   }
   
   /**
    * Returns 1 if the input is true, 0 otherwise
    * 
    * @param input
    * @return
    */
   public static int convertFromBooleanToInt(boolean input)
   {
	   return (input == true) ? 1 : 0;
   }   
   
   /**
    * N.B.: it works only with array of integers
    *
    * If the input is 
    * [43,543,34543]
    * the output would be an array containing three elements:
    * 43   543    and   34543
    *
    * 
    * @param input
    * @return
    */
   public static String[] fromJsonArrayToStringArray(String input)
   {
	   return input.substring(1, input.length()-1).split(",");
   }
}