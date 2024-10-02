package app.entities;

public class Lists
{
    private int listId;
    private String name;
    private int userId;

    public Lists(int listId, String name, int userId)
    {
        this.listId = listId;
        this.name = name;
        this.userId = userId;
    }

    public int getListId()
    {
        return listId;
    }

    public String getName()
    {
        return name;
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
                ", name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}