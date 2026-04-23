using LAB10.Enum;

namespace LAB10.Domain;

public class SortingTask: Task
{
    private int[] numbers;
    private SortStrategy sortStrategy;
    private AbstractSorter abstractSorter;
    
    public SortingTask(string taskId, string descriere, int[] numbers, SortStrategy sortStrategy) : base(taskId, descriere)
    {
        this.numbers = numbers;
        this.sortStrategy = sortStrategy;
        switch (sortStrategy)
        {
            case SortStrategy.BUBBLESORT:
                abstractSorter = new BubbleSort();
                break;
            case SortStrategy.QUICKSORT:
                abstractSorter = new QuickSort();
                break;
            default:
                throw new Exception("Invalid sort strategy");
        }
    }

    public override void execute()
    {
        abstractSorter.sort(numbers);
        Console.WriteLine($"Sorted array using {sortStrategy}: {string.Join(", ", numbers)}");
    }
}