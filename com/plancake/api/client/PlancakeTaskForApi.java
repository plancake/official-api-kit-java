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

public class PlancakeTaskForApi
{
	public long id = 0L;
	public int listId = 0;
	public String description = null;
	public int sortOrder = 0;
	public boolean isStarred = false;
	public boolean isHeader = false;
	public String dueDate = null; // format: yyyy-mm-dd
        public String dueTime = null; // in the (H)Hmm 24h format (i.e.: 915, 1913)
	public int repetitionId = 0;
	public int repetitionParam = 0;
        public String repetitionIcalRrule = null;
	public boolean isCompleted = false;
	public boolean isFromSystem = false;
	public String note = null;
	public String tagIds = null; // comma-separated list of tag IDs
	public long createdAt = 0;
	public long updatedAt = 0;
	
	public PlancakeTaskForApi()
	{
		this.id = 0L;
		this.listId = 0;
		this.description = null;
		this.sortOrder = 0;
		this.isStarred = false;
		this.isHeader = false;
		this.dueDate = null;
		this.dueTime = null;
		this.repetitionId = 0;
		this.repetitionParam = 0;
		this.isCompleted = false;
		this.isFromSystem = false;
		this.note = null;
		this.tagIds = null; // comma-separated list of tag IDs
		this.createdAt = 0;
		this.updatedAt = 0;
	}
}