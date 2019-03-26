// Copyright (c) .NET Foundation. All rights reserved.
// Licensed under the Apache License, Version 2.0. See License.txt in the project root for license information.

namespace Microsoft.AspNetCore.Mvc.ModuleFramework
{
    public interface IModuleFilter
    {
        void OnModuleExecuting(ModuleExecutingContext context);
        void OnModuleExecuted(ModuleExecutedContext context);
    }
}
