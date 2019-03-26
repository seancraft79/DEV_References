﻿// Copyright (c) .NET Foundation. All rights reserved.
// Licensed under the Apache License, Version 2.0. See License.txt in the project root for license information.

using Newtonsoft.Json;

namespace Antiforgery.Angular1
{
    public class TodoItem
    {
        [JsonProperty(PropertyName = "name")]
        public string Name { get; set; }
    }
}
