package dev.keva.core.command.impl.list;

import dev.keva.core.command.annotation.CommandImpl;
import dev.keva.core.command.annotation.Execute;
import dev.keva.core.command.annotation.Mutate;
import dev.keva.core.command.annotation.ParamLength;
import dev.keva.ioc.annotation.Autowired;
import dev.keva.ioc.annotation.Component;
import dev.keva.protocol.resp.reply.BulkReply;
import dev.keva.protocol.resp.reply.MultiBulkReply;
import dev.keva.protocol.resp.reply.Reply;
import dev.keva.store.KevaDatabase;

@Component
@CommandImpl("lpop")
@ParamLength(type = ParamLength.Type.AT_LEAST, value = 1)
@Mutate
public class LPop {
    private final KevaDatabase database;

    @Autowired
    public LPop(KevaDatabase database) {
        this.database = database;
    }

    @Execute
    public Reply<?> execute(byte[] key, byte[] count) {
        if (count == null) {
            byte[] got = database.lpop(key);
            return got == null ? BulkReply.NIL_REPLY : new BulkReply(got);
        }

        int countInt = Integer.parseInt(new String(count));
        Reply<?>[] replies = new Reply[countInt];
        for (int i = 0; i < countInt; i++) {
            byte[] got = database.lpop(key);
            replies[i] = got == null ? BulkReply.NIL_REPLY : new BulkReply(got);
        }
        return new MultiBulkReply(replies);
    }
}
