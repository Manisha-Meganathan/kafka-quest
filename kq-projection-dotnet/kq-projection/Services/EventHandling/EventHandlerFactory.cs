using KQ_Projection.Common.Enums;
using KQ_Projection.Common.Models.Keys;

namespace KQ_Projection.Services.EventHandling;

public class EventHandlerFactory
{
    public static EventHandler GetHandler(AggregateKey key) {
        return key.eventType switch {
            EventType.GAME_STARTED_EVENT => new GameStartedEventHandler(),
            EventType.PIECE_ADDED_EVENT => new PieceAddedEventHandler(),
            EventType.PIECE_REMOVED_EVENT => new PieceRemovedEventHandler(),
            EventType.EXCEPTIONAL_EVENT => new ExceptionalEventHandler(),
            EventType.DISCARD_GAME_EVENT => new DiscardGameEventHandler(),
            _ => throw new ArgumentNullException(nameof(key))
        };
    }
}
