package app.entities;

public class Lists
{
    private int listId;
    private String listname;
    private int userId;

    public Lists(int listId, String listname, int userId)
    {
        this.listId = listId;
        this.listname = listname;
        this.userId = userId;
    }

    public int getListId()
    {
        return listId;
    }

    public String getName()
    {
        return listname;
    }


    public int getUserId()
    {
        return userId;
    }

    @Override
    public String toString()
    {
        return "List{" +
                "listId=" + listId +
                ", name='" + listname + '\'' +
                ", userId=" + userId +
                '}';
    }
}