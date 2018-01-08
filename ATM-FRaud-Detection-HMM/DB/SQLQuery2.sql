USE [HMM]
GO

/****** Object:  Table [dbo].[Customer]    Script Date: 01/10/2014 12:32:49 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[Customer](
	[Cust_name] [nvarchar](50) NULL,
	[Address] [nvarchar](50) NULL,
	[Phone_number] [nvarchar](50) NULL,
	[City] [varchar](50) NULL,
	[EmailID] [varchar](50) NULL,
	[Ac_number] [int] NULL,
	[ThumbData] [varbinary](max) NULL
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


