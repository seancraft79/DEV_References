using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Linq.Expressions;
using Microsoft.AspNetCore.Routing;
using Microsoft.AspNetCore.Html;
using Microsoft.AspNetCore.Mvc.ViewFeatures;
using Microsoft.AspNetCore.Mvc.ViewFeatures.Internal;

namespace Microsoft.AspNetCore.Mvc.Rendering
{
    public static class AngularExtensions
    {
        public static IHtmlContent ngPasswordFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression)
        {
            return html.ngPasswordFor(expression, null);
        }

        public static IHtmlContent ngPasswordFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression, object htmlAttributes)
        {
            return html.ngPasswordFor(expression, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static IHtmlContent ngPasswordFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression, IDictionary<string, object> htmlAttributes)
        {
            return html.ngTextBoxFor(expression, MergeAttributes(
                new RouteValueDictionary { { "type", "password" } },
                htmlAttributes));
        }

        public static IHtmlContent ngTextBoxFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression)
        {
            return html.ngTextBoxFor(expression, new RouteValueDictionary());
        }

        public static IHtmlContent ngTextBoxFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression, object htmlAttributes)
        {
            return html.ngTextBoxFor(expression, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static IHtmlContent ngTextBoxFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression, IDictionary<string, object> htmlAttributes)
        {
            var expressionText = ExpressionHelper.GetExpressionText(expression);
            var modelExplorer = ExpressionMetadataProvider.FromLambdaExpression(expression, html.ViewData, html.MetadataProvider);
            var ngAttributes = new Dictionary<string, object>();

            ngAttributes["type"] = "text";

            // Angular binding to client-side model (scope). This is required for Angular validation to work.
            var valueFieldName = html.ViewData.TemplateInfo.GetFullHtmlFieldName(expressionText);
            ngAttributes["name"] = valueFieldName;
            ngAttributes["ng-model"] = valueFieldName;

            // Set input type
            if (string.Equals(modelExplorer.Metadata.DataTypeName, Enum.GetName(typeof(DataType), DataType.EmailAddress), StringComparison.OrdinalIgnoreCase))
            {
                ngAttributes["type"] = "email";
            }
            else if (modelExplorer.ModelType == typeof(Uri)
                     || string.Equals(modelExplorer.Metadata.DataTypeName, Enum.GetName(typeof(DataType), DataType.Url), StringComparison.OrdinalIgnoreCase)
                     || string.Equals(modelExplorer.Metadata.DataTypeName, Enum.GetName(typeof(DataType), DataType.ImageUrl), StringComparison.OrdinalIgnoreCase))
            {
                ngAttributes["type"] = "url";
            }
            else if (IsNumberType(modelExplorer.ModelType))
            {
                ngAttributes["type"] = "number";
                if (IsIntegerType(modelExplorer.ModelType))
                {
                    ngAttributes["step"] = "1";
                }
                else
                {
                    ngAttributes["step"] = "any";
                }
            }
            else if (modelExplorer.ModelType == typeof(DateTime))
            {
                if (string.Equals(modelExplorer.Metadata.DataTypeName, Enum.GetName(typeof(DataType), DataType.Date), StringComparison.OrdinalIgnoreCase))
                {
                    ngAttributes["type"] = "date";
                }
                else if (string.Equals(modelExplorer.Metadata.DataTypeName, Enum.GetName(typeof(DataType), DataType.DateTime), StringComparison.OrdinalIgnoreCase))
                {
                    ngAttributes["type"] = "datetime";
                }
            }

            // Add attributes for Angular validation
            foreach (var validator in modelExplorer.Metadata.ValidatorMetadata.OfType<ValidationAttribute>())
            {
                if (validator is StringLengthAttribute)
                {
                    var lengthValidator = (StringLengthAttribute)validator;
                    if (lengthValidator.MinimumLength != 0)
                    {
                        ngAttributes["ng-minlength"] = lengthValidator.MinimumLength;
                    }
                    if (lengthValidator.MaximumLength != int.MaxValue)
                    {
                        ngAttributes["ng-maxlength"] = lengthValidator.MaximumLength;
                    }
                }
                else if (validator is RequiredAttribute)
                {
                    ngAttributes["required"] = null;
                }
                else if (validator is RangeAttribute)
                {
                    var rangeValidator = (RangeAttribute)validator;
                    ngAttributes["min"] = rangeValidator.Minimum;
                    ngAttributes["max"] = rangeValidator.Maximum;
                }
                else if (validator is CompareAttribute)
                {
                    // CompareAttribute validator
                    var compareValidator = (CompareAttribute)validator;
                    ngAttributes["app-equal-to"] = compareValidator.OtherProperty;
                    // TODO: Actually write the Angular directive to use this
                }
                // TODO: Regex, Phone(regex)
            }

            // Render!
            if (modelExplorer.Model != null)
            {
                ngAttributes.Add("value", modelExplorer.Model.ToString());
            }

            var tag = new TagBuilder("input");
            tag.MergeAttributes(MergeAttributes(ngAttributes, htmlAttributes));
            tag.TagRenderMode = TagRenderMode.SelfClosing;
            return tag;
        }

        private static bool IsNumberType(Type type)
        {
            if (type == typeof(Int16) ||
                type == typeof(Int32) ||
                type == typeof(Int64) ||
                type == typeof(UInt16) ||
                type == typeof(UInt32) ||
                type == typeof(UInt64) ||
                type == typeof(Decimal) ||
                type == typeof(Double) ||
                type == typeof(Single))
            {
                return true;
            }
            return false;
        }

        //private static bool IsIntegerType(Type type)
        //{
        //    switch (Type.GetTypeCode(type))
        //    {
        //        case TypeCode.Int16:
        //        case TypeCode.Int32:
        //        case TypeCode.Int64:
        //        case TypeCode.UInt16:
        //        case TypeCode.UInt32:
        //        case TypeCode.UInt64:
        //            return true;
        //    }
        //    return false;
        //}

        private static bool IsIntegerType(Type type)
        {
            if (type == typeof(Int16) ||
                type == typeof(Int32) ||
                type == typeof(Int64) ||
                type == typeof(UInt16) ||
                type == typeof(UInt32) ||
                type == typeof(UInt64))
            {
                return true;
            }
            return false;
        }

        public static IHtmlContent ngDropDownListFor<TModel, TProperty, TDisplayProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> propertyExpression, Expression<Func<TModel, TDisplayProperty>> displayExpression, string source, string nullOption, object htmlAttributes)
        {
            return ngDropDownListFor(html, propertyExpression, displayExpression, source, nullOption, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static IHtmlContent ngDropDownListFor<TModel, TProperty, TDisplayProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> propertyExpression, Expression<Func<TModel, TDisplayProperty>> displayExpression, string source, string nullOption, IDictionary<string, object> htmlAttributes)
        {
            var propertyExpressionText = ExpressionHelper.GetExpressionText(propertyExpression);
            var displayExpressionText = ExpressionHelper.GetExpressionText(displayExpression);
            var metadata = ExpressionMetadataProvider.FromLambdaExpression(propertyExpression, html.ViewData, html.MetadataProvider);
            var tag = new TagBuilder("select");

            var valueFieldName = html.ViewData.TemplateInfo.GetFullHtmlFieldName(propertyExpressionText);
            var displayFieldName = html.ViewData.TemplateInfo.GetFullHtmlFieldName(displayExpressionText);

            var displayFieldNameParts = displayFieldName.Split('.');
            displayFieldName = displayFieldNameParts[displayFieldNameParts.Length - 1];

            tag.Attributes["id"] = html.Id(propertyExpressionText);
            tag.Attributes["name"] = valueFieldName;
            tag.Attributes["ng-model"] = valueFieldName;

            var ngOptionsFormat = "a.{0} as a.{1} for a in {2}";
            var ngOptions = string.Format(ngOptionsFormat, valueFieldName, displayFieldName, source);
            tag.Attributes["ng-options"] = ngOptions;

            if (nullOption != null)
            {
                var nullOptionTag = new TagBuilder("option");
                nullOptionTag.Attributes["value"] = string.Empty;
                nullOptionTag.InnerHtml.SetContent(nullOption);
                tag.InnerHtml.SetHtmlContent(nullOptionTag);
            }

            var isRequired = metadata.Metadata.ValidatorMetadata.OfType<RequiredAttribute>().Any();
            if (isRequired)
            {
                tag.Attributes["required"] = string.Empty;
            }

            tag.MergeAttributes(htmlAttributes, replaceExisting: true);

            return tag;
        }

        public static IHtmlContent ngValidationMessageFor<TModel, TProperty>(this IHtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string formName)
        {
            return ngValidationMessageFor(htmlHelper, expression, formName, ((IDictionary<string, object>)new RouteValueDictionary()));
        }

        public static IHtmlContent ngValidationMessageFor<TModel, TProperty>(this IHtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression, string formName, object htmlAttributes)
        {
            return ngValidationMessageFor(htmlHelper, expression, formName, HtmlHelper.AnonymousObjectToHtmlAttributes(htmlAttributes));
        }

        public static IHtmlContent ngValidationMessageFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression, string formName, IDictionary<string, object> htmlAttributes)
        {
            var expressionText = ExpressionHelper.GetExpressionText(expression);
            var metadata = ExpressionMetadataProvider.FromLambdaExpression(expression, html.ViewData, html.MetadataProvider);
            var modelName = html.ViewData.TemplateInfo.GetFullHtmlFieldName(expressionText);

            var tags = new List<TagBuilder>();

            // Get validation messages from data type
            // TODO: How to get validation messages from model metadata? All methods/properties required seem protected internal :(

            foreach (var validator in metadata.Metadata.ValidatorMetadata.OfType<ValidationAttribute>())
            {
                var tag = new TagBuilder("span");
                tag.Attributes["ng-cloak"] = string.Empty;

                if (validator is RequiredAttribute)
                {
                    tag.Attributes["ng-show"] = string.Format("({0}.submitAttempted || {0}.{1}.$dirty || {0}.{1}.visited) && {0}.{1}.$error.{2}", formName, modelName, "required");
                    tag.InnerHtml.SetContent(validator.ErrorMessage);
                }
                else if (validator is StringLengthAttribute)
                {
                    tag.Attributes["ng-show"] = string.Format("({0}.submitAttempted || {0}.{1}.$dirty || {0}.{1}.visited) && ({0}.{1}.$error.minlength || {0}.{1}.$error.maxlength)",
                        formName, modelName);
                    tag.InnerHtml.SetContent(validator.ErrorMessage);
                }
                else if (validator is RangeAttribute)
                {
                    tag.Attributes["ng-show"] = string.Format("({0}.submitAttempted || {0}.{1}.$dirty || {0}.{1}.visited) && ({0}.{1}.$error.min || {0}.{1}.$error.max)",
                        formName, modelName);
                    tag.InnerHtml.SetContent(validator.ErrorMessage);
                }
                // TODO: Regex, equalto, remote
                else
                {
                    continue;
                }

                tag.MergeAttributes(htmlAttributes);
                tags.Add(tag);
            }

            return new HtmlString(String.Concat(tags.Select(t => t.ToString())));
        }

        public static string ngValidationClassFor<TModel, TProperty>(this IHtmlHelper<TModel> html, Expression<Func<TModel, TProperty>> expression, string formName, string className)
        {
            var expressionText = ExpressionHelper.GetExpressionText(expression);
            var metadata = ExpressionMetadataProvider.FromLambdaExpression(expression, html.ViewData, html.MetadataProvider);
            var modelName = html.ViewData.TemplateInfo.GetFullHtmlFieldName(expressionText);
            var ngClassFormat = "{{ '{0}' : ({1}.submitAttempted || {1}.{2}.$dirty || {1}.{2}.visited) && {1}.{2}.$invalid }}";

            return string.Format(ngClassFormat, className, formName, modelName);
        }

        private static IDictionary<string, object> MergeAttributes(IDictionary<string, object> source, IDictionary<string, object> target)
        {
            if (target == null)
            {
                return source;
            }

            // Keys in target win over keys in source
            foreach (var pair in source)
            {
                if (!target.ContainsKey(pair.Key))
                {
                    target[pair.Key] = pair.Value;
                }
            }

            return target;
        }
    }
}