using KQ_Projection.Common.Models.Jigsaw;
using System.Runtime.CompilerServices;

namespace KQ_Projection.Services;

public class ProjectionManager
{
    private static ProjectionManager? instance;
    private readonly Dictionary<Guid?, JigsawPuzzleProjection> gameSessionProjections;

    private ProjectionManager()
    {
        gameSessionProjections = new Dictionary<Guid?, JigsawPuzzleProjection>();
    }

    [MethodImpl(MethodImplOptions.Synchronized)]
    public static ProjectionManager GetInstance()
    {
        instance ??= new ProjectionManager();
        return instance;
    }

    [MethodImpl(MethodImplOptions.Synchronized)]
    public JigsawPuzzleProjection? GetProjection(Guid? gameId)
    {
        gameSessionProjections.TryGetValue(gameId, out JigsawPuzzleProjection? result);
        return result;
    }

    [MethodImpl(MethodImplOptions.Synchronized)]
    public void AddProjection(JigsawPuzzleProjection jigsawPuzzleGame)
    {
        Console.WriteLine($"Adding to projection manager, gameId: {jigsawPuzzleGame.gameId}");
        gameSessionProjections.Add(jigsawPuzzleGame.gameId, jigsawPuzzleGame);
        Console.WriteLine("Projection added!");
    }

    [MethodImpl(MethodImplOptions.Synchronized)]
    public void RemoveProjection(Guid gameId)
    {
        Console.WriteLine($"Removing projection with gameId: {gameId}");
        gameSessionProjections.Remove(gameId);
        Console.WriteLine("Projection Removed!");
    }
}
