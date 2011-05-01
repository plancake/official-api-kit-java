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

public class PlancakeTasksFilterOptions
{
	public long fromTimestamp = 0L; // to return only the tasks created or edited since this timestamp
	public long toTimestamp = 0L; // to return only the tasks created or edited till this timestamp
	public long taskId = 0L; // if > 0, it would return only a specific task
	public int listId = 0;
	public int tagId = 0;
	public boolean completed = false;
	public boolean onlyWithDueDate = false;
	public boolean onlyWithoutDueDate = false;
	public boolean onlyDueTodayOrTomorrow = false;	
	public boolean onlyStarred = false;
	
	public PlancakeTasksFilterOptions()
	{
		this.fromTimestamp = 0L;
		this.toTimestamp = 0L;
		this.taskId = 0L;
		this.listId = 0;
		this.tagId = 0;
		this.completed = false;
		this.onlyWithDueDate = false;
		this.onlyWithoutDueDate = false;
		this.onlyDueTodayOrTomorrow = false;	
		this.onlyStarred = false;		
	}
	
}